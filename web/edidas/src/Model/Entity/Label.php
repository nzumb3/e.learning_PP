<?php
namespace App\Model\Entity;

use Cake\ORM\Entity;

/**
 * Label Entity
 *
 * @property int $id
 * @property string $name
 * @property string $abbreviation
 * @property int $user_id
 *
 * @property \App\Model\Entity\User $user
 */
class Label extends Entity
{

    /**
     * Fields that can be mass assigned using newEntity() or patchEntity().
     *
     * Note that when '*' is set to true, this allows all unspecified fields to
     * be mass assigned. For security purposes, it is advised to set '*' to false
     * (or remove it), and explicitly make individual fields accessible as needed.
     *
     * @var array
     */
    protected $_accessible = [
        'name' => true,
        'abbreviation' => true,
        'user_id' => true,
        'user' => true
    ];
}
