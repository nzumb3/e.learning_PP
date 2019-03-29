<?php
/**
 * @var \App\View\AppView $this
 * @var \App\Model\Entity\Appointment[]|\Cake\Collection\CollectionInterface $appointments
 */
?>
<nav class="large-3 medium-4 columns" id="actions-sidebar">
    <ul class="side-nav">
        <li class="heading"><?= __('Actions') ?></li>
        <?= $userId == 1 ? "<li>".$this->Html->link(__('List Users'), ['controller' => 'Users', 'action' => 'index'])."</li>" : '' ?>
        <?= $userId == 1 ? "<li>".$this->Html->link(__('Add User'), ['controller' => 'Users', 'action' => 'add'])."</li>" : '' ?>
        <li><?= $this->Html->link(__('List Labels'), ['controller' => 'Labels', 'action' => 'index']) ?></li>
        <li><?= $this->Html->link(__('New Label'), ['controller' => 'Labels', 'action' => 'add']) ?></li>
        <li><?= $this->Html->link(__('List Appointments'), ['action' => 'index']) ?></li>
        <li><?= $this->Html->link(__('New Appointment'), ['action' => 'add']) ?></li>
        <li><?= $this->Html->link(__('Upload Appointments'), ['action' => 'upload']) ?></li>
        <li><?= $this->Html->link(__('Logout'), ['controller' => 'Users', 'action' => 'logout']) ?></li>
    </ul>
</nav>
<div class="appointments index large-9 medium-8 columns content">
    <h3><?= __('Appointments') ?></h3>
    <table cellpadding="0" cellspacing="0">
        <thead>
            <tr>
                <th scope="col" width="10%"><?= $this->Paginator->sort('appointment_content.label_id', 'Label') ?></th>
                <th scope="col" width="25%"><?= $this->Paginator->sort('appointment_content_id') ?></th>
                <th scope="col" width="15%"><?= $this->Paginator->sort('start_date') ?></th>
                <th scope="col" width="15%"><?= $this->Paginator->sort('end_date') ?></th>
                <th scope="col" class="actions"><?= __('Actions') ?></th>
            </tr>
        </thead>
        <tbody>
            <?php foreach ($appointments as $appointment): ?>
            <tr>
                <td width="10%"><?= $appointment->has('appointment_content') ? $appointment->appointment_content->label->name : '' ?></td>
                <td width="25%"><?= $appointment->has('appointment_content') ? $this->Html->link($appointment->appointment_content->title, ['controller' => 'AppointmentContents', 'action' => 'view', $appointment->appointment_content->id]) : '' ?></td>
                <td width="15%"><?= h($appointment->start_date) ?></td>
                <td width="15%"><?= h($appointment->end_date) ?></td>
                <td class="actions">
                    <?= $this->Html->link(__('View'), ['action' => 'view', $appointment->id]) ?> |
                    <?= $this->Html->link(__('Edit'), ['action' => 'edit', $appointment->id]) ?> |
                    <?= $this->Form->postLink(__('Delete'), ['action' => 'delete', $appointment->id], ['confirm' => __('Are you sure you want to delete this appointment?')]) ?> |
                    <?= $this->Form->postLink(__('Delete all appointments of content'), ['controller' => 'AppointmentContents', 'action' => 'delete', $appointment->appointment_content->id], ['confirm' => __('Are you sure you want to delete all appointments of "{0}"?', $appointment->appointment_content->title)]) ?>
                </td>
            </tr>
            <?php endforeach; ?>
        </tbody>
    </table>
    <div class="paginator">
        <ul class="pagination">
            <?= $this->Paginator->first('<< ' . __('first')) ?>
            <?= $this->Paginator->prev('< ' . __('previous')) ?>
            <?= $this->Paginator->numbers() ?>
            <?= $this->Paginator->next(__('next') . ' >') ?>
            <?= $this->Paginator->last(__('last') . ' >>') ?>
        </ul>
        <p><?= $this->Paginator->counter(['format' => __('Page {{page}} of {{pages}}, showing {{current}} record(s) out of {{count}} total')]) ?></p>
    </div>
</div>
