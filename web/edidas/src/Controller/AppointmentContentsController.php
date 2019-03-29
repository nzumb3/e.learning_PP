<?php
namespace App\Controller;

use App\Controller\AppController;

/**
 * AppointmentContents Controller
 *
 * This controller can be used, if appointment contents by itself have to be managed.
 * Usually an appointment content can be added via the insertion of a new appointment.
 *
 * The delete-method is called from the appointments-index view.
 *
 * @property \App\Model\Table\AppointmentContentsTable $AppointmentContents
 *
 * @method \App\Model\Entity\AppointmentContent[]|\Cake\Datasource\ResultSetInterface paginate($object = null, array $settings = [])
 */
class AppointmentContentsController extends AppController
{

    /**
     * Index method
     *
     * @return \Cake\Http\Response|void
     */
    public function index()
    {
        $appointmentContents = $this->paginate($this->AppointmentContents);

        $this->set(compact('appointmentContents'));
    }

    /**
     * View method
     *
     * @param string|null $id Appointment Content id.
     * @return \Cake\Http\Response|void
     * @throws \Cake\Datasource\Exception\RecordNotFoundException When record not found.
     */
    public function view($id = null)
    {
        $appointmentContent = $this->AppointmentContents->get($id, [
            'contain' => ['Appointments']
        ]);

        $this->set('appointmentContent', $appointmentContent);
    }

    /**
     * Edit method
     *
     * @param string|null $id Appointment Content id.
     * @return \Cake\Http\Response|null Redirects on successful edit, renders view otherwise.
     * @throws \Cake\Datasource\Exception\RecordNotFoundException When record not found.
     */
    public function edit($id = null)
    {
        $appointmentContent = $this->AppointmentContents->get($id, [
            'contain' => []
        ]);
        if ($this->request->is(['patch', 'post', 'put'])) {
            $appointmentContent = $this->AppointmentContents->patchEntity($appointmentContent, $this->request->getData());
            if ($this->AppointmentContents->save($appointmentContent)) {
                $this->Flash->success(__('The appointment content has been saved.'));

                return $this->redirect(['action' => 'index']);
            }
            $this->Flash->error(__('The appointment content could not be saved. Please, try again.'));
        }
        $this->set(compact('appointmentContent'));
    }

    /**
     * Delete method
     *
     * By calling this method on a appointment content, all appointments, that are related with the appointment content
     * are deleted too.
     *
     * @param string|null $id Appointment Content id.
     * @return \Cake\Http\Response|null Redirects to index.
     * @throws \Cake\Datasource\Exception\RecordNotFoundException When record not found.
     */
    public function delete($id = null)
    {
        $this->request->allowMethod(['post', 'delete']);
        $appointmentContent = $this->AppointmentContents->get($id);
        if ($this->AppointmentContents->delete($appointmentContent)) {
            $this->Flash->success(__('The appointment content has been deleted.'));
        } else {
            $this->Flash->error(__('The appointment content could not be deleted. Please, try again.'));
        }

        return $this->redirect(['controller' => 'Appointments', 'action' => 'index']);
    }
}
