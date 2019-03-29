<?php
/**
 * @var \App\View\AppView $this
 * @var \App\Model\Entity\AppointmentContent $appointmentContent
 */
?>
<nav class="large-3 medium-4 columns" id="actions-sidebar">
    <ul class="side-nav">
        <li class="heading"><?= __('Actions') ?></li>
        <li><?= $this->Form->postLink(
                __('Delete'),
                ['action' => 'delete', $appointmentContent->id],
                ['confirm' => __('Are you sure you want to delete # {0}?', $appointmentContent->id)]
            )
        ?></li>
        <li><?= $this->Html->link(__('List Appointment Contents'), ['action' => 'index']) ?></li>
        <li><?= $this->Html->link(__('List Appointments'), ['controller' => 'Appointments', 'action' => 'index']) ?></li>
        <li><?= $this->Html->link(__('New Appointment'), ['controller' => 'Appointments', 'action' => 'add']) ?></li>
    </ul>
</nav>
<div class="appointmentContents form large-9 medium-8 columns content">
    <?= $this->Form->create($appointmentContent) ?>
    <fieldset>
        <legend><?= __('Edit Appointment Content') ?></legend>
        <?php
            echo $this->Form->control('title');
            echo $this->Form->control('description');
            echo $this->Form->control('abbreviation');
            echo $this->Form->control('room');
            echo $this->Form->control('color');
            echo $this->Form->control('label_id');
        ?>
    </fieldset>
    <?= $this->Form->button(__('Submit')) ?>
    <?= $this->Form->end() ?>
</div>
