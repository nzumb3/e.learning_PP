<?php
namespace App\Model\Table;

use Cake\ORM\Query;
use Cake\ORM\RulesChecker;
use Cake\ORM\Table;
use Cake\Validation\Validator;

/**
 * AppointmentContents Model
 *
 * @property |\Cake\ORM\Association\BelongsTo $Labels
 * @property \App\Model\Table\AppointmentsTable|\Cake\ORM\Association\HasMany $Appointments
 *
 * @method \App\Model\Entity\AppointmentContent get($primaryKey, $options = [])
 * @method \App\Model\Entity\AppointmentContent newEntity($data = null, array $options = [])
 * @method \App\Model\Entity\AppointmentContent[] newEntities(array $data, array $options = [])
 * @method \App\Model\Entity\AppointmentContent|bool save(\Cake\Datasource\EntityInterface $entity, $options = [])
 * @method \App\Model\Entity\AppointmentContent|bool saveOrFail(\Cake\Datasource\EntityInterface $entity, $options = [])
 * @method \App\Model\Entity\AppointmentContent patchEntity(\Cake\Datasource\EntityInterface $entity, array $data, array $options = [])
 * @method \App\Model\Entity\AppointmentContent[] patchEntities($entities, array $data, array $options = [])
 * @method \App\Model\Entity\AppointmentContent findOrCreate($search, callable $callback = null, $options = [])
 */
class AppointmentContentsTable extends Table
{

    /**
     * Initialize method
     *
     * @param array $config The configuration for the Table.
     * @return void
     */
    public function initialize(array $config)
    {
        parent::initialize($config);

        $this->setTable('appointment_contents');
        $this->setDisplayField('title');
        $this->setPrimaryKey('id');

        $this->belongsTo('Labels', [
            'foreignKey' => 'label_id',
            'joinType' => 'INNER'
        ]);
        $this->hasMany('Appointments', [
            'foreignKey' => 'appointment_content_id',
            'dependent' => true,
            'cascadeCallbacks' => true,
        ]);
    }

    /**
     * Default validation rules.
     *
     * @param \Cake\Validation\Validator $validator Validator instance.
     * @return \Cake\Validation\Validator
     */
    public function validationDefault(Validator $validator)
    {
        $validator
            ->integer('id')
            ->allowEmptyString('id', 'create');

        $validator
            ->scalar('title')
            ->maxLength('title', 255)
            ->requirePresence('title', 'create')
            ->allowEmptyString('title', false);

        $validator
            ->scalar('description')
            ->requirePresence('description', 'create')
            ->allowEmptyString('description', false);

        $validator
            ->scalar('abbreviation')
            ->maxLength('abbreviation', 10)
            ->requirePresence('abbreviation', 'create')
            ->allowEmptyString('abbreviation', false);

        $validator
            ->scalar('room')
            ->maxLength('room', 50)
            ->requirePresence('room', 'create')
            ->allowEmptyString('room', false);

        $validator
            ->scalar('color')
            ->maxLength('color', 6)
            ->requirePresence('color', 'create')
            ->allowEmptyString('color', false);

        return $validator;
    }

    /**
     * Returns a rules checker object that will be used for validating
     * application integrity.
     *
     * @param \Cake\ORM\RulesChecker $rules The rules object to be modified.
     * @return \Cake\ORM\RulesChecker
     */
    public function buildRules(RulesChecker $rules)
    {
        $rules->add($rules->existsIn(['label_id'], 'Labels'));

        return $rules;
    }
}
