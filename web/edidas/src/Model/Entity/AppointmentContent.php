<?php
namespace App\Model\Entity;

use Cake\ORM\Entity;

/**
 * AppointmentContent Entity
 *
 * @property int $id
 * @property string $title
 * @property string $description
 * @property string $abbreviation
 * @property string $room
 * @property string $color
 * @property int $label_id
 *
 * @property \App\Model\Entity\Appointment[] $appointments
 */
class AppointmentContent extends Entity
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
        'title' => true,
        'description' => true,
        'abbreviation' => true,
        'room' => true,
        'color' => true,
        'label_id' => true,
        'appointments' => true
    ];
}
