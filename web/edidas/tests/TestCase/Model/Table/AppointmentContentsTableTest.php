<?php
namespace App\Test\TestCase\Model\Table;

use App\Model\Table\AppointmentContentsTable;
use Cake\ORM\TableRegistry;
use Cake\TestSuite\TestCase;

/**
 * App\Model\Table\AppointmentContentsTable Test Case
 */
class AppointmentContentsTableTest extends TestCase
{

    /**
     * Test subject
     *
     * @var \App\Model\Table\AppointmentContentsTable
     */
    public $AppointmentContents;

    /**
     * Fixtures
     *
     * @var array
     */
    public $fixtures = [
        'app.AppointmentContents',
        'app.Appointments'
    ];

    /**
     * setUp method
     *
     * @return void
     */
    public function setUp()
    {
        parent::setUp();
        $config = TableRegistry::getTableLocator()->exists('AppointmentContents') ? [] : ['className' => AppointmentContentsTable::class];
        $this->AppointmentContents = TableRegistry::getTableLocator()->get('AppointmentContents', $config);
    }

    /**
     * tearDown method
     *
     * @return void
     */
    public function tearDown()
    {
        unset($this->AppointmentContents);

        parent::tearDown();
    }

    /**
     * Test initialize method
     *
     * @return void
     */
    public function testInitialize()
    {
        $this->markTestIncomplete('Not implemented yet.');
    }

    /**
     * Test validationDefault method
     *
     * @return void
     */
    public function testValidationDefault()
    {
        $this->markTestIncomplete('Not implemented yet.');
    }
}
