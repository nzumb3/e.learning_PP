<?php
/**
 * @var \App\View\AppView $this
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
<div class="uploads form large-9 medium-8 columns content">
    <?= $this->Form->create(false, ['type' => 'file']) ?>
    <fieldset>
        <legend><?= __('Upload File') ?></legend>
        <?php
        echo $this->Form->file('submittedFile');
        echo $this->Form->control('label_id');
        ?>
    </fieldset>
    <?= $this->Form->button(__('Upload')) ?>
    <?= $this->Form->end() ?>
</div>