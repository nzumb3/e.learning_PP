<?php
/**
 * @var \App\View\AppView $this
 * @var \App\Model\Entity\Label $label
 */
?>
<nav class="large-3 medium-4 columns" id="actions-sidebar">
    <ul class="side-nav">
        <li class="heading"><?= __('Actions') ?></li>
        <li><?= $this->Form->postLink(
                __('Delete'),
                ['action' => 'delete', $label->id],
                ['confirm' => __('Are you sure you want to delete # {0}?', $label->id)]
            )
        ?></li>
        <li><?= $this->Html->link(__('List Labels'), ['action' => 'index']) ?></li>
        <li><?= $this->Html->link(__('List Users'), ['controller' => 'Users', 'action' => 'index']) ?></li>
        <li><?= $this->Html->link(__('New User'), ['controller' => 'Users', 'action' => 'add']) ?></li>
    </ul>
</nav>
<div class="labels form large-9 medium-8 columns content">
    <?= $this->Form->create($label) ?>
    <fieldset>
        <legend><?= __('Edit Label') ?></legend>
        <?php
            echo $this->Form->control('name');
            echo $this->Form->control('abbreviation');
            echo $this->Form->control('user_id', ['options' => $users]);
        ?>
    </fieldset>
    <?= $this->Form->button(__('Submit')) ?>
    <?= $this->Form->end() ?>
</div>
