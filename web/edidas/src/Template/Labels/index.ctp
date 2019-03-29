<?php
/**
 * @var \App\View\AppView $this
 * @var \App\Model\Entity\Label[]|\Cake\Collection\CollectionInterface $labels
 */
?>
<nav class="large-3 medium-4 columns" id="actions-sidebar">
    <ul class="side-nav">
        <li class="heading"><?= __('Actions') ?></li>
        <?= $userId == 1 ? "<li>".$this->Html->link(__('List Users'), ['controller' => 'Users', 'action' => 'index'])."</li>" : '' ?>
        <?= $userId == 1 ? "<li>".$this->Html->link(__('Add User'), ['controller' => 'Users', 'action' => 'add'])."</li>" : '' ?>
        <li><?= $this->Html->link(__('List Labels'), ['action' => 'index']) ?></li>
        <li><?= $this->Html->link(__('New Label'), ['action' => 'add']) ?></li>
        <li><?= $this->Html->link(__('List Appointments'), ['controller' => 'Appointments', 'action' => 'index']) ?></li>
        <li><?= $this->Html->link(__('New Appointment'), ['controller' => 'Appointments', 'action' => 'add']) ?></li>
        <li><?= $this->Html->link(__('Upload Appointments'), ['controller' => 'Appointments', 'action' => 'upload']) ?></li>
        <li><?= $this->Html->link(__('Logout'), ['controller' => 'Users', 'action' => 'logout']) ?></li>
    </ul>
</nav>
<div class="labels index large-9 medium-8 columns content">
    <h3><?= __('Labels') ?></h3>
    <table cellpadding="0" cellspacing="0">
        <thead>
            <tr>
                <th scope="col"><?= $this->Paginator->sort('name') ?></th>
                <th scope="col"><?= $this->Paginator->sort('abbreviation') ?></th>
                <th scope="col" class="actions"><?= __('Actions') ?></th>
            </tr>
        </thead>
        <tbody>
            <?php foreach ($labels as $label): ?>
            <tr>
                <td><?= h($label->name) ?></td>
                <td><?= h($label->abbreviation) ?></td>
                <td class="actions">
                    <?= $this->Html->link(__('Edit'), ['action' => 'edit', $label->id]) ?>
                    <?= $this->Form->postLink(__('Delete'), ['action' => 'delete', $label->id], ['confirm' => __('Are you sure you want to delete "{0}"? Note, that  all appointments belonging to this labels will also be deleted.', $label->name)]) ?>
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
