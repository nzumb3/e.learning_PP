<?php
/**
 * @var \App\View\AppView $this
 * @var \App\Model\Entity\AppointmentContent[]|\Cake\Collection\CollectionInterface $appointmentContents
 */
?>
<nav class="large-3 medium-4 columns" id="actions-sidebar">
    <ul class="side-nav">
        <li class="heading"><?= __('Actions') ?></li>
        <li><?= $this->Html->link(__('New Appointment Content'), ['action' => 'add']) ?></li>
        <li><?= $this->Html->link(__('List Appointments'), ['controller' => 'Appointments', 'action' => 'index']) ?></li>
        <li><?= $this->Html->link(__('New Appointment'), ['controller' => 'Appointments', 'action' => 'add']) ?></li>
    </ul>
</nav>
<div class="appointmentContents index large-9 medium-8 columns content">
    <h3><?= __('Appointment Contents') ?></h3>
    <table cellpadding="0" cellspacing="0">
        <thead>
            <tr>
                <th scope="col"><?= $this->Paginator->sort('id') ?></th>
                <th scope="col"><?= $this->Paginator->sort('title') ?></th>
                <th scope="col"><?= $this->Paginator->sort('abbreviation') ?></th>
                <th scope="col"><?= $this->Paginator->sort('room') ?></th>
                <th scope="col"><?= $this->Paginator->sort('color') ?></th>
                <th scope="col"><?= $this->Paginator->sort('label_id') ?></th>
                <th scope="col" class="actions"><?= __('Actions') ?></th>
            </tr>
        </thead>
        <tbody>
            <?php foreach ($appointmentContents as $appointmentContent): ?>
            <tr>
                <td><?= $this->Number->format($appointmentContent->id) ?></td>
                <td><?= h($appointmentContent->title) ?></td>
                <td><?= h($appointmentContent->abbreviation) ?></td>
                <td><?= h($appointmentContent->room) ?></td>
                <td><?= h($appointmentContent->color) ?></td>
                <td><?= $this->Number->format($appointmentContent->label_id) ?></td>
                <td class="actions">
                    <?= $this->Html->link(__('View'), ['action' => 'view', $appointmentContent->id]) ?>
                    <?= $this->Html->link(__('Edit'), ['action' => 'edit', $appointmentContent->id]) ?>
                    <?= $this->Form->postLink(__('Delete'), ['action' => 'delete', $appointmentContent->id], ['confirm' => __('Are you sure you want to delete # {0}?', $appointmentContent->id)]) ?>
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
