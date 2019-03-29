<?php
namespace App\Controller;

use App\Controller\AppController;
use Cake\I18n\FrozenTime;
use Cake\I18n\Time;

/**
 * Appointments Controller
 *
 * This controller is used for managing appointments. It provides the following methods:
 * index: An overview of the appointments created by the authenticated user
 * add: The insertion of a new appointment with relating appointment content
 * delete: The deletion of a single appointment
 * upload: An upload function for a .ics-file, that is parsed and inserted into the database
 *
 * Other function, that are not available in the user interface are:
 * view: The presentation of a single appointment
 * edit: To edit a single appointment
 *
 * @property \App\Model\Table\AppointmentsTable $Appointments
 *
 * @method \App\Model\Entity\Appointment[]|\Cake\Datasource\ResultSetInterface paginate($object = null, array $settings = [])
 */
class AppointmentsController extends AppController
{

    /**
     * Index method
     *
     * @return \Cake\Http\Response|void
     */
    public function index()
    {
        $this->paginate = [
            'contain' => ['AppointmentContents', 'Users', 'AppointmentContents.Labels']
        ];
        $appointments = $this->paginate($this->Appointments);

        $this->set(compact('appointments'));
        $this->set('userId', $this->Auth->user('id'));
    }

    /**
     * View method
     *
     * @param string|null $id Appointment id.
     * @return \Cake\Http\Response|void
     * @throws \Cake\Datasource\Exception\RecordNotFoundException When record not found.
     */
    public function view($id = null)
    {
        $appointment = $this->Appointments->get($id, [
            'contain' => ['AppointmentContents', 'Users']
        ]);

        $this->set('appointment', $appointment);
    }

    /**
     * Add method
     *
     * This method adds an appointment and it's content.
     *
     * @return \Cake\Http\Response|null Redirects on successful add, renders view otherwise.
     */
    public function add()
    {
        $appointment = $this->Appointments->newEntity();
        if ($this->request->is('post')) {
            $appointment = $this->Appointments->patchEntity($appointment, $this->request->getData(), [
                'associated' => ['AppointmentContents']
            ]);
            $appointment->user_id = $this->Auth->user('id');
            if ($this->Appointments->save($appointment)) {
                $this->Flash->success(__('The appointment has been saved.'));

                return $this->redirect(['action' => 'index']);
            }
            $this->Flash->error(__('The appointment could not be saved. Please, try again.'));
        }
        $labels = $this->Appointments->AppointmentContents->Labels->find('list', ['limit' => 200]);
        $this->set(compact('appointment', 'labels'));
        $this->set('userId', $this->Auth->user('id'));
    }

    /**
     * Edit method
     *
     * @param string|null $id Appointment id.
     * @return \Cake\Http\Response|null Redirects on successful edit, renders view otherwise.
     * @throws \Cake\Datasource\Exception\RecordNotFoundException When record not found.
     */
    public function edit($id = null)
    {
        $appointment = $this->Appointments->get($id, [
            'contain' => []
        ]);
        if ($this->request->is(['patch', 'post', 'put'])) {
            $appointment = $this->Appointments->patchEntity($appointment, $this->request->getData());
            if ($this->Appointments->save($appointment)) {
                $this->Flash->success(__('The appointment has been saved.'));

                return $this->redirect(['action' => 'index']);
            }
            $this->Flash->error(__('The appointment could not be saved. Please, try again.'));
        }
        $appointmentContents = $this->Appointments->AppointmentContents->find('list', ['limit' => 200]);
        $users = $this->Appointments->Users->find('list', ['limit' => 200]);
        $this->set(compact('appointment', 'appointmentContents', 'users'));
    }

    /**
     * Delete method
     *
     * @param string|null $id Appointment id.
     * @return \Cake\Http\Response|null Redirects to index.
     * @throws \Cake\Datasource\Exception\RecordNotFoundException When record not found.
     */
    public function delete($id = null)
    {
        $this->request->allowMethod(['post', 'delete']);
        $appointment = $this->Appointments->get($id);
        if ($this->Appointments->delete($appointment)) {
            $this->Flash->success(__('The appointment has been deleted.'));
        } else {
            $this->Flash->error(__('The appointment could not be deleted. Please, try again.'));
        }

        return $this->redirect(['action' => 'index']);
    }

    /**
     * This method parses an uploaded .ics-file with a specific format and adds the parsed appointments with content
     * relating to the given label into the database.
     */
    public function upload()
    {
        $submittedFile = array();
        $appointments = array();
        $lines = array();
        if ($this->request->is('post')) {
            //Load the parameters of the uploaded file
            $submittedFile = $this->request->getData('submittedFile');
            $label_id = $this->request->getData('label_id');
            //Open the .ics-file
            $lines = file($submittedFile['tmp_name'], FILE_IGNORE_NEW_LINES);

            //Iterate over each line in the .ics-file
            foreach($lines as $line_num => $line) {

                //Search for the beginning line of an event
                if(strpos($line, 'BEGIN:VEVENT') !== false) {
                    $appointmentContent = $this->Appointments->AppointmentContents->newEntity();

                    //Process the location(s) of an event
                    $location_line = $lines[$line_num + 6];

                    //Split multiple locations
                    $splitted_locations = explode('||', $location_line);
                    if(strpos($splitted_locations[1], '\n') !== false) {
                        $location = substr($splitted_locations[1], 0, strpos($splitted_locations[1], '\n'));
                    } else {
                        $location = $splitted_locations[1];
                    }

                    //Process the description of the event as the following substring after the colon
                    $summary = explode(':', $lines[$line_num + 5])[1];

                    //Fill the appointment content
                    $appointmentContent->title = $summary;
                    $appointmentContent->description = $summary;
                    $appointmentContent->abbreviation = substr($summary, 0, 5);
                    $appointmentContent->label_id = $label_id;
                    $appointmentContent->room = $location;

                    $appointment = $this->Appointments->newEntity();

                    //Process the startdate
                    $startline = $lines[$line_num + 3];
                    $timestring = explode('=', $startline)[1];
                    $splitted_timestring = explode(':', $timestring);
                    $timezone = $splitted_timestring[0];
                    $timestamp = $splitted_timestring[1];
                    $appointment->start_date = Time::createFromFormat('Ymd\THis', $timestamp, $timezone)->i18nFormat('yyyy-MM-dd HH:mm:ss');

                    //Process the enddate
                    $endline = $lines[$line_num + 4];
                    $timestring = explode('=', $endline)[1];
                    $splitted_timestring = explode(':', $timestring);
                    $timezone = $splitted_timestring[0];
                    $timestamp = $splitted_timestring[1];
                    $appointment->end_date = Time::createFromFormat('Ymd\THis', $timestamp, $timezone)->i18nFormat('yyyy-MM-dd HH:mm:ss');

                    $appointment->user_id = $this->Auth->user('id');
                    $appointment->appointment_content = $appointmentContent;

                    $this->Appointments->save($appointment);
                }
            }
        }
        $labels = $this->Appointments->AppointmentContents->Labels->find('list', ['limit' => 200]);
        $this->set(compact('submittedFile', 'appointments', 'lines', 'labels'));
        $this->set('userId', $this->Auth->user('id'));
    }
}
