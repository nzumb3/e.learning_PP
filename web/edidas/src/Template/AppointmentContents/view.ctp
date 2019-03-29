<?php
/**
 * @var \App\View\AppView $this
 * @var \App\Model\Entity\AppointmentContent $appointmentContent
 */
?>
<nav class="large-3 medium-4 columns" id="actions-sidebar">
    <ul class="side-nav">
        <li class="heading"><?= __('Actions') ?></li>
        <li><?= $this->Html->link(__('Edit Appointment Content'), ['action' => 'edit', $appointmentContent->id]) ?> </li>
        <li><?= $this->Form->postLink(__('Delete Appointment Content'), ['action' => 'delete', $appointmentContent->id], ['confirm' => __('Are you sure you want to delete # {0}?', $appointmentContent->id)]) ?> </li>
        <li><?= $this->Html->link(__('List Appointment Contents'), ['action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Appointment Content'), ['action' => 'add']) ?> </li>
        <li><?= $this->Html->link(__('List Appointments'), ['controller' => 'Appointments', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Appointment'), ['controller' => 'Appointments', 'action' => 'add']) ?> </li>
    </ul>
</nav>
<div class="appointmentContents view large-9 medium-8 columns content">
    <h3><?= h($appointmentContent->title) ?></h3>
    <table class="vertical-table">
        <tr>
            <th scope="row"><?= __('Title') ?></th>
            <td><?= h($appointmentContent->title) ?></td>
        </tr>
        <tr>
            <th scope="row"><?= __('Abbreviation') ?></th>
            <td><?= h($appointmentContent->abbreviation) ?></td>
        </tr>
        <tr>
            <th scope="row"><?= __('Room') ?></th>
            <td><?= h($appointmentContent->room) ?></td>
        </tr>
        <tr>
            <th scope="row"><?= __('Color') ?></th>
            <td><?= h($appointmentContent->color) ?></td>
        </tr>
        <tr>
            <th scope="row"><?= __('Id') ?></th>
            <td><?= $this->Number->format($appointmentContent->id) ?></td>
        </tr>
        <tr>
            <th scope="row"><?= __('Label Id') ?></th>
            <td><?= $this->Number->format($appointmentContent->label_id) ?></td>
        </tr>
    </table>
    <div class="row">
        <h4><?= __('Description') ?></h4>
        <?= $this->Text->autoParagraph(h($appointmentContent->description)); ?>
    </div>
    <div class="related">
        <h4><?= __('Related Appointments') ?></h4>
        <?php if (!empty($appointmentContent->appointments)): ?>
        <table cellpadding="0" cellspacing="0">
            <tr>
                <th scope="col"><?= __('Id') ?></th>
                <th scope="col"><?= __('Start Date') ?></th>
                <th scope="col"><?= __('End Date') ?></th>
                <th scope="col"><?= __('Appointment Content Id') ?></th>
                <th scope="col"><?= __('User Id') ?></th>
                <th scope="col" class="actions"><?= __('Actions') ?></th>
            </tr>
            <?php foreach ($appointmentContent->appointments as $appointments): ?>
            <tr>
                <td><?= h($appointments->id) ?></td>
                <td><?= h($appointments->start_date) ?></td>
                <td><?= h($appointments->end_date) ?></td>
                <td><?= h($appointments->appointment_content_id) ?></td>
                <td><?= h($appointments->user_id) ?></td>
                <td class="actions">
                    <?= $this->Html->link(__('View'), ['controller' => 'Appointments', 'action' => 'view', $appointments->id]) ?>
                    <?= $this->Html->link(__('Edit'), ['controller' => 'Appointments', 'action' => 'edit', $appointments->id]) ?>
                    <?= $this->Form->postLink(__('Delete'), ['controller' => 'Appointments', 'action' => 'delete', $appointments->id], ['confirm' => __('Are you sure you want to delete # {0}?', $appointments->id)]) ?>
                </td>
            </tr>
            <?php endforeach; ?>
        </table>
        <?php endif; ?>
    </div>
</div>
