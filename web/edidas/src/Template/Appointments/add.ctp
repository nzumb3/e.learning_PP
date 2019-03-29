<?php
/**
 * @var \App\View\AppView $this
 * @var \App\Model\Entity\Appointment $appointment
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
<div class="appointments form large-9 medium-8 columns content">
    <?= $this->Form->create($appointment) ?>
    <fieldset>
        <legend><?= __('Add Appointment') ?></legend>
        <?php
            echo $this->Form->control('appointment_content.title');
            echo $this->Form->control('appointment_content.description');
            echo $this->Form->control('appointment_content.abbreviation');
            echo $this->Form->control('appointment_content.room');
            echo $this->Form->control('appointment_content.color');
            echo $this->Form->control('appointment_content.label_id');
            echo $this->Form->control('start_date');
            echo $this->Form->control('end_date');
            //echo $this->Form->control('appointment_content_id', ['options' => $appointmentContents]);
        ?>
    </fieldset>
    <?= $this->Form->button(__('Submit')) ?>
    <?= $this->Form->end() ?>
</div>
