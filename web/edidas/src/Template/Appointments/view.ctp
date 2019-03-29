<?php
/**
 * @var \App\View\AppView $this
 * @var \App\Model\Entity\Appointment $appointment
 */
?>
<nav class="large-3 medium-4 columns" id="actions-sidebar">
    <ul class="side-nav">
        <li class="heading"><?= __('Actions') ?></li>
        <li><?= $this->Html->link(__('Edit Appointment'), ['action' => 'edit', $appointment->id]) ?> </li>
        <li><?= $this->Form->postLink(__('Delete Appointment'), ['action' => 'delete', $appointment->id], ['confirm' => __('Are you sure you want to delete # {0}?', $appointment->id)]) ?> </li>
        <li><?= $this->Html->link(__('List Appointments'), ['action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Appointment'), ['action' => 'add']) ?> </li>
        <li><?= $this->Html->link(__('List Appointment Contents'), ['controller' => 'AppointmentContents', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Appointment Content'), ['controller' => 'AppointmentContents', 'action' => 'add']) ?> </li>
        <li><?= $this->Html->link(__('List Users'), ['controller' => 'Users', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New User'), ['controller' => 'Users', 'action' => 'add']) ?> </li>
    </ul>
</nav>
<div class="appointments view large-9 medium-8 columns content">
    <h3><?= h($appointment->id) ?></h3>
    <table class="vertical-table">
        <tr>
            <th scope="row"><?= __('Appointment Content') ?></th>
            <td><?= $appointment->has('appointment_content') ? $this->Html->link($appointment->appointment_content->title, ['controller' => 'AppointmentContents', 'action' => 'view', $appointment->appointment_content->id]) : '' ?></td>
        </tr>
        <tr>
            <th scope="row"><?= __('User') ?></th>
            <td><?= $appointment->has('user') ? $this->Html->link($appointment->user->id, ['controller' => 'Users', 'action' => 'view', $appointment->user->id]) : '' ?></td>
        </tr>
        <tr>
            <th scope="row"><?= __('Id') ?></th>
            <td><?= $this->Number->format($appointment->id) ?></td>
        </tr>
        <tr>
            <th scope="row"><?= __('Start Date') ?></th>
            <td><?= h($appointment->start_date) ?></td>
        </tr>
        <tr>
            <th scope="row"><?= __('End Date') ?></th>
            <td><?= h($appointment->end_date) ?></td>
        </tr>
    </table>
</div>
