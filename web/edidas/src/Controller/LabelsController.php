<?php
namespace App\Controller;

use App\Controller\AppController;

/**
 * Labels Controller
 *
 * This controller is used for managing the labels. It provides the following methods:
 * index: An overview of the labels created by the authenticated user
 * add: The insertion of a new label
 * delete: The deletion of a label, all appointment contents and appointments belonging to this label
 *
 * Other function, that are not available in the user interface are:
 * view: The presentation of a single label
 * edit: To edit a single label
 *
 *
 * @property \App\Model\Table\LabelsTable $Labels
 *
 * @method \App\Model\Entity\Label[]|\Cake\Datasource\ResultSetInterface paginate($object = null, array $settings = [])
 */
class LabelsController extends AppController
{

    /**
     * Index method
     *
     * @return \Cake\Http\Response|void
     */
    public function index()
    {
        $this->paginate = [
            'contain' => ['Users']
        ];
        $labels = $this->paginate($this->Auth->user('labels'));

        $this->set(compact('labels'));
        $this->set('userId', $this->Auth->user('id'));
    }

    /**
     * Add method
     *
     * @return \Cake\Http\Response|null Redirects on successful add, renders view otherwise.
     */
    public function add()
    {
        $label = $this->Labels->newEntity();
        if ($this->request->is('post')) {
            $label = $this->Labels->patchEntity($label, $this->request->getData());
            $label->user_id = $this->Auth->user('id');
            if ($this->Labels->save($label)) {
                $this->Flash->success(__('The label has been saved.'));

                return $this->redirect(['action' => 'index']);
            }
            $this->Flash->error(__('The label could not be saved. Please, try again.'));
        }
        $this->set(compact('label'));
        $this->set('userId', $this->Auth->user('id'));
    }

    /**
     * Edit method
     *
     * @param string|null $id Label id.
     * @return \Cake\Http\Response|null Redirects on successful edit, renders view otherwise.
     * @throws \Cake\Datasource\Exception\RecordNotFoundException When record not found.
     */
    public function edit($id = null)
    {
        $label = $this->Labels->get($id, [
            'contain' => []
        ]);
        if ($this->request->is(['patch', 'post', 'put'])) {
            $label = $this->Labels->patchEntity($label, $this->request->getData());
            if ($this->Labels->save($label)) {
                $this->Flash->success(__('The label has been saved.'));

                return $this->redirect(['action' => 'index']);
            }
            $this->Flash->error(__('The label could not be saved. Please, try again.'));
        }
        $users = $this->Labels->Users->find('list', ['limit' => 200]);
        $this->set(compact('label', 'users'));
    }

    /**
     * Delete method
     *
     * By calling this method on a label, it deletes also all appointment contents and though all appointments, that are
     * related to this label
     *
     * @param string|null $id Label id.
     * @return \Cake\Http\Response|null Redirects to index.
     * @throws \Cake\Datasource\Exception\RecordNotFoundException When record not found.
     */
    public function delete($id = null)
    {
        $this->request->allowMethod(['post', 'delete']);
        $label = $this->Labels->get($id);
        if ($this->Labels->delete($label)) {
            $this->Flash->success(__('The label has been deleted.'));
        } else {
            $this->Flash->error(__('The label could not be deleted. Please, try again.'));
        }

        return $this->redirect(['action' => 'index']);
    }
}
