package org.usfirst.frc.team6391.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
//import org.opencv.core.Mat;
//import org.opencv.imgproc.Imgproc;
//import edu.wpi.cscore.CvSink;
//import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after   
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	final String defaultAuto = "Default"; 
	
//controller
	Joystick joystick;
	XboxController xBox; 
	XboxController xBox2;
//solenoids
	DoubleSolenoid clawGrab;
	DoubleSolenoid claw90;
	DoubleSolenoid ElevatorStop;
	DoubleSolenoid BigTom;
    DoubleSolenoid FrontSolenoid;
//compressor
	Compressor comp;
//sparks
	Spark leftFrontMotor;
	Spark leftRearMotor;
	Spark rightFrontMotor;
	Spark rightRearMotor;

	Spark climb;
	Spark winch;
//	Talon backStiltLeft;
//	Talon frontStiltLeft;
	Spark backStiltLeft;
	Spark frontStiltLeft;
//	Talon spinMotorLeft;
	Spark spinMotorLeft;
	Spark spinMotorRight;
//	Talon spinMotorRight;
	Encoder backStiltEncoder;
	Encoder backStiltLeftEncoder;
	Encoder frontStiltLeftEncoder;
	Encoder frontStiltEncoder;

	Spark frontStilt;
	Spark backStilt;
	Spark backStiltMotor;
		//SAE 23 Mar			WPI_VictorSPX leftFrontMotor;
		//SAE 23 Mar			WPI_VictorSPX leftRearMotor;
		//SAE 23 Mar			WPI_VictorSPX rightFrontMotor;
		//SAE 23 Mar			WPI_VictorSPX rightRearMotor;
	
//camera	
//	CameraServer camera = CameraServer.getInstance();
//	CameraServer camera2 = CameraServer.getInstance();
UsbCamera camera1;
UsbCamera camera2;
VideoSink cameraServer;
	
//RobotDrive drive;
	DifferentialDrive drive;
	
//auto	
	final String customAuto = "My Auto";
	int autoSelected;
	SendableChooser<Integer> chooser = new SendableChooser<>();
	SendableChooser<Integer> chooser2 = new SendableChooser<>();
	SendableChooser<Integer> chooser3 = new SendableChooser<>();
	int count; 

//sensors
	AnalogInput FrontWheelDistanceSensor;
	AnalogInput RearWheelDistanceSensor;
//	AnalogInput MidWheelDistanceSensor;
	double temp = 0;
	int finalStateCount;
	int backEncoderCount;
	int backLeftEncoderCount;
	int frontEncoderCount;
	int frontLeftEncoderCount;
	boolean onetime;
	int errorBLCount;
	int errorFLCount;
	int errorCount;
	double backStiltPower;
	double frontStiltPower;
	double backStiltLeftPower;
	double frontStiltLeftPower;

	boolean manualOverride;
	boolean manualOverride2;

// Teleop States for backing onto platform	
	private final static int TELEOP_STATE_1 = 1;
    private final static int TELEOP_STATE_2 = 2;
    private final static int TELEOP_STATE_3 = 3;
    private final static int TELEOP_STATE_4 = 4;
    private final static int TELEOP_STATE_5 = 5;
    private final static int TELEOP_STATE_6 = 6;
    private final static int TELEOP_STATE_7 = 7;
    private final static int TELEOP_STATE_8 = 8;
    private final static int TELEOP_STATE_9 = 9;
    private final static int TELEOP_STATE_10 = 10;
    private final static int TELEOP_STATE_11 = 11;
    private final static int TELEOP_STATE_12 = 12;
    private final static int TELEOP_STATE_13 = 13;
    private final static int TELEOP_STATE_14 = 14;
	private final static int TELEOP_STATE_15 = 15;
	private int teleopState;
	private boolean teleopAuton;
	private int autonState_9_Complete;


//auton states	
	private Timer autonStateTimer;
	private int autonState;
	private final static int AUTON_STATE_1 = 1;
    private final static int AUTON_STATE_2 = 2;
    private final static int AUTON_STATE_3 = 3;
    private final static int AUTON_STATE_4 = 4;
    private final static int AUTON_STATE_5 = 5;
    private final static int AUTON_STATE_6 = 6;
    private final static int AUTON_STATE_7 = 7;
    private final static int AUTON_STATE_8 = 8;
    private final static int AUTON_STATE_9 = 9;
    private final static int AUTON_STATE_10 = 10;
    private final static int AUTON_STATE_11 = 11;
    private final static int AUTON_STATE_12 = 12;
    private final static int AUTON_STATE_13 = 13;
    private final static int AUTON_STATE_14 = 14;
    private final static int AUTON_STATE_15 = 15;
    private int selectedStation;
    private int selectedStation2;
    private int selectedStation3;

//booleans
    boolean LiftBigTomFlag; 
    boolean AutonomousDropBox;
    boolean AutonomousFirstPrint;
  	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */



	@Override
	public void robotInit() {
		
	//climb
		climb = new Spark (0);
		
	// Drive
		leftFrontMotor=new Spark(1);
		leftRearMotor=new Spark(2);
		rightFrontMotor=new Spark(3); 
		rightRearMotor=new Spark(4);
		SpeedControllerGroup m_left = new SpeedControllerGroup(leftFrontMotor, leftRearMotor);
		SpeedControllerGroup m_right = new SpeedControllerGroup(rightFrontMotor, rightRearMotor);
		
	//Stilts
		frontStilt = new Spark (6);	
		backStilt = new Spark (11);
		backStiltMotor = new Spark (8);
//		backStiltLeft = new Talon (10);
//		backStiltLeft = new Talon (6);
		backStiltLeft = new Spark (5);
//		frontStiltLeft = new Talon (11);
//		frontStiltLeft = new Talon (8);
		frontStiltLeft = new Spark (10);
// Gripper
//		spinMotorLeft=new Talon (9);	
		spinMotorLeft=new Spark (9);	
		spinMotorRight=new Spark (7);
//		spinMotorRight=new Talon (7);
		backStiltEncoder = new Encoder(0, 1, true);
		frontStiltEncoder = new Encoder(2, 3, true);
		backStiltLeftEncoder = new Encoder(4, 5, true);
		frontStiltLeftEncoder = new Encoder(6, 7, true);
				
		
	//drive=new RobotDrive(0,1,2,3); 
		drive=new DifferentialDrive(m_left, m_right);

	// Define Solenoid control
		// PCM ID 0
		clawGrab= new DoubleSolenoid(0,1);
		claw90= new DoubleSolenoid(4,5);	
		
		// PCM ID 1
	// Opening solenoid control on 2 PCM,ID = 1
		FrontSolenoid= new DoubleSolenoid(1,0,1);
		comp=new Compressor(0);


	//distance sensors
		FrontWheelDistanceSensor= new AnalogInput(0);
		RearWheelDistanceSensor= new AnalogInput(1);


	//xbox
		xBox=new XboxController(0);
		xBox2=new XboxController(1);
		count=0;
		joystick=new Joystick(0);

	//auto chooser shuffleboard
		AutonomousDropBox = true;
		chooser.addDefault("Auto Left", 1);
    	chooser.addObject("Auto Center", 2);
    	chooser.addObject("Auto Right", 3);
		SmartDashboard.putData("Auto choices", chooser);
		chooser2.addDefault("Drop", 1);
		chooser2.addObject("No Drop", 2);
		SmartDashboard.putData("Auto choices Drop/No Drop", chooser2);
		chooser3.addDefault("7th Route",1);
		SmartDashboard.putData("Auto choices 7", chooser3);
		
		SmartDashboard.putData ("Front Sensor:", FrontWheelDistanceSensor);
		SmartDashboard.putData ("Rear Sensor:", RearWheelDistanceSensor);
		backEncoderCount = 0;
		backLeftEncoderCount = 0;
		frontEncoderCount = 0;
		temp = 0;
		finalStateCount = 0;
		onetime = true;
		errorBLCount = 0;
		errorFLCount = 0;
		errorCount = 0;
		backStiltPower = -0.75;
		frontStiltPower = -0.75;
		backStiltLeftPower = -0.75;
		frontStiltLeftPower = -0.75;

		manualOverride = false;
		manualOverride2 = false;

		SmartDashboard.putNumber("Back Enc:", backEncoderCount);
		SmartDashboard.putNumber("Front Enc:", frontEncoderCount);
		SmartDashboard.putNumber("B-Left Enc:", backLeftEncoderCount);
		SmartDashboard.putNumber("F-Left Enc:", frontLeftEncoderCount);
		SmartDashboard.putNumber("FError:", errorCount);
		SmartDashboard.putNumber("BLError:", errorBLCount);
		SmartDashboard.putNumber("FLError:", errorFLCount);
		SmartDashboard.putNumber("Back St Pwr:", backStiltPower);
		SmartDashboard.putNumber("Front St Pwr:", frontStiltPower);
		SmartDashboard.putNumber("B-Left St Pwr:", backStiltLeftPower);
		SmartDashboard.putNumber("F-Left St Pwr:", frontStiltLeftPower);
	// camera
		CameraServer.getInstance().startAutomaticCapture(0);
		CameraServer.getInstance().startAutomaticCapture(1);
//SAE		camera1 = CameraServer.getInstance().startAutomaticCapture(0);
//SAE 		camera2 = CameraServer.getInstance().startAutomaticCapture(1);
//SAE 		cameraServer = CameraServer.getInstance().getServer();
//SAE 		camera1.setConnectionStrategy(VideoSource::ConnectionStrategy::kConnectionKeepOpen);
//SAE		camera2.setConnectionStrategy(VideoSource::ConnectionStrategy::kConnectionKeepOpen);
		

	//Big Tom
		// Set Boolean so Big Tom Cylinder does not lift by default
		LiftBigTomFlag = false;
		BigTom= new DoubleSolenoid(6,7);

	
	} // End of RoboInit();

	// method used to change states in Autonomous only -- not used in Teleop "Auto"
	private void changeAutonState(int nextState) {
	    	if (nextState != autonState) {
	    		autonState = nextState;
	    		autonStateTimer.reset();
	    	}
	    
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		//A DropList will be in the SmartDashboard/Shuffleboard where you will be able too choose a value.
        //
    	//and to get the selected value in the code :
    	//and depending on the selected value you could use a conditional structure to execute a specific autonomous code like :
		selectedStation = chooser.getSelected();
		selectedStation2 = chooser2.getSelected();
		selectedStation3 = chooser3.getSelected();

		// Change starting state to AUTON_STATE_2 at beginning since we will be 
		// hanging over the platform with front wheels over.
		autonState = AUTON_STATE_1;
		// Clear flag used to tell user in autonomous that  Buzz is on the ground.
		autonState_9_Complete = 0;
		SmartDashboard.putNumber("autonState_9_Complete:", autonState_9_Complete);

    	autonStateTimer = new Timer();
    	autonStateTimer.start();
    	 
    	SendableChooser<Integer> autoChooser = new SendableChooser<>();

	} // AutonomousInit

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
public void autonomousPeriodic() {
	
	
	boolean left_side = true;
	String gameData;

	SmartDashboard.putData ("Front Sensor:", FrontWheelDistanceSensor);
	SmartDashboard.putData ("Rear Sensor:", RearWheelDistanceSensor);

//	// We have manual control in Autonomous for 2019, so....
//	// Allow ability to drive
//	drive.arcadeDrive(xBox.getRawAxis(5)*1,xBox.getRawAxis(4)*1);
//  // Allow ability to move back stilts
//	backStilt.set(-xBox.getRawAxis(1)); //front stilts will drop


	switch (autonState) {
		case AUTON_STATE_1 :
		{
			spinMotorLeft.set(-.7);
			spinMotorRight.set(-.7);
			if (autonStateTimer.hasPeriodPassed(0.4)) 
			{ 
				changeAutonState(AUTON_STATE_12);
			}
			break;
		}
		case AUTON_STATE_12 : 
		{
			spinMotorLeft.set(0);
			spinMotorRight.set(0);
//			changeAutonState(AUTON_STATE_2);
			changeAutonState(AUTON_STATE_13);
			break;
		}
		case AUTON_STATE_13 :
		{
			climb.set(1);
			if (autonStateTimer.hasPeriodPassed(0.4)) 
			{ 
   				changeAutonState(AUTON_STATE_14);
			}
			break;
		}
		case AUTON_STATE_14 :
		{
			climb.set(0);
			changeAutonState(AUTON_STATE_2);
			break;
		}
		case AUTON_STATE_2 :  
		{
			// set both front stilts to go down

			frontStilt.set(-1); //front stilts will drop
			//spinMotorLeft.set(-1);
			frontStiltLeft.set (-1);
			//time allotted for stilts to drop
			if (autonStateTimer.hasPeriodPassed(1.5)) 
			{
				changeAutonState(AUTON_STATE_3);
			}
			break;
		}

		case AUTON_STATE_3 :
		{
			// turn off front stilt motors	
			frontStilt.set(-0.1); 
			//spinMotorLeft.set(-0.05);
			frontStiltLeft.set (-0.05);
			drive.arcadeDrive(0.5,0); //drive forward
			//if sensor indicates that back legs are off ledge then do the following
			temp = RearWheelDistanceSensor.getVoltage();
			SmartDashboard.putNumber("Temp:", temp);
			//		if (FrontWheelDistanceSensor.getVoltage() < 1.2 ) 
			if (temp < 1.2 )
			{
				changeAutonState(AUTON_STATE_4);
			}
			break;
		}

		case AUTON_STATE_4 :
		{
			// turn off drive motors	
			drive.arcadeDrive(0,0); //stop driving
			changeAutonState(AUTON_STATE_5);
			break;
		}

		case AUTON_STATE_5 :
		{
			// Drop the back stilts
			backStilt.set(-1); 
			//spinMotorRight.set(-1);
			backStiltLeft.set (-1);
			if (autonStateTimer.hasPeriodPassed(1.8)) //time allotted for stilts to drop
			{
				changeAutonState(AUTON_STATE_6);
			}
			break;
		}
		case AUTON_STATE_6 :
		{
			SmartDashboard.putString ("AUTOSTATE6","Auton State 6:");
			// stop back stilt motor
			backStilt.set(-0.05); 
			//spinMotorRight.set(-0.05);
			backStiltLeft.set (-0.05);
			backStiltMotor.set (.41); //start driving until off ledge
			if (autonStateTimer.hasPeriodPassed(.5)) //time allotted for driving off
//Test			if (autonStateTimer.hasPeriodPassed(5)) //time allotted for driving off
			{
				changeAutonState(AUTON_STATE_7);
			}
			break;
		}
	
		case AUTON_STATE_7 :
		{
			SmartDashboard.putString ("AUTOSTATE7","Auton State 7:");
			backStiltMotor.set (0.0); //stop driving on stilts
		//	frontStilt.set(0.5); //raise front stilts
		//	backStilt.set(1); //raise back stilts
			// Raise back stilts
			// Back
			backStilt.set(1); 
//			spinMotorRight.set (1);
			backStiltLeft.set (1);
	
			//time allotted for stilts to raise
			if(autonStateTimer.hasPeriodPassed(1.5)); 
			{
				// Skip state 7 for now
				changeAutonState(AUTON_STATE_8);
			}
			break;		
		}
					
		case AUTON_STATE_8:
		{
			SmartDashboard.putString ("AUTOSTATE8","Auton State 8:");
			// Raise Front Stilts
			frontStilt.set(1); 
		//	spinMotorLeft.set(1);
			frontStiltLeft.set (1);
		//	drive.arcadeDrive(-.5,0); // drive past the line
			if(autonStateTimer.hasPeriodPassed(2))
			{
				changeAutonState(AUTON_STATE_9);
			}
			break;
		}
		case AUTON_STATE_9 :
		{
			SmartDashboard.putString ("AUTOSTATE8a","Auton State 8a:");
			// Stop Back Stilts
			backStilt.set (0);
//			spinMotorRight.set(0);
			backStiltLeft.set (0);
			// Stop Front Stilts
			frontStilt.set(0);
//			spinMotorLeft.set(0);
			frontStiltLeft.set (0);

//			autonState_9_Complete = 0;
//			if(autonStateTimer.hasPeriodPassed(5))
//			{
//				changeAutonState(AUTON_STATE_10);
//			}
			changeAutonState(AUTON_STATE_10);
			break;
		}
	
		case AUTON_STATE_10 :
		{
			SmartDashboard.putString ("AUTOSTATE9","Auton State 9:");
			autonState_9_Complete = 100000000;
			SmartDashboard.putNumber("autonState_9_Complete:", autonState_9_Complete);
				// DRIVE - right joystick
			drive.arcadeDrive(xBox.getRawAxis(5)*-1,xBox.getRawAxis(4)*1); 

			// ELEVATOR - left joystick
			climb.set(-xBox2.getRawAxis(1)); 

			// FRONT STILTS DOWN - top right bumper button
			if(xBox.getBumper(Hand.kLeft))
			{
    			System.out.println("Top Right Bumper Button");
    			backStilt.set(-1);
			}

			// FRONT STILTS UP - top right trigger		
			backStilt.set((xBox.getTriggerAxis(Hand.kLeft)*1)); 

			// BACK STILTS DOWN - top left bumper button
			if(xBox.getBumper(Hand.kRight)) 
			{
	    		frontStilt.set(-1);	
			}
			// BACK STILTS UP - top left trigger
			frontStilt.set((xBox.getTriggerAxis(Hand.kRight)*1)); 
				
			//controller 2
		
			// INTAKE - right joystick
			spinMotorLeft.set(xBox2.getRawAxis(5)*-0.7); // Used for both Walter and Buzz?
			spinMotorRight.set(xBox2.getRawAxis(5)*-0.7); // Comment when testing with Walter
		
			break;
		}
	} // End of Switch
	    	
}// AutonomousPeriodic
		
@Override
public void testInit() {

} // End of testInit()


	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void testPeriodic(){

		SmartDashboard.putData ("Front Sensor:", FrontWheelDistanceSensor);
		SmartDashboard.putData ("Rear Sensor:", RearWheelDistanceSensor);
		backEncoderCount = backStiltEncoder.get();
		frontEncoderCount = frontStiltEncoder.get();
		backLeftEncoderCount = backStiltLeftEncoder.get();
		frontLeftEncoderCount = frontStiltLeftEncoder.get();
		SmartDashboard.putNumber("Back Enc:", backEncoderCount);
		SmartDashboard.putNumber("Front Enc:", frontEncoderCount);
		SmartDashboard.putNumber("B-Left Enc:", backLeftEncoderCount);
		SmartDashboard.putNumber("F-Left Enc:", frontLeftEncoderCount);
		
//		drive.arcadeDrive(-0.5,0);
		drive.arcadeDrive(xBox.getRawAxis(5)*1,xBox.getRawAxis(4)*1);
		// INTAKE - right joystick
		frontStilt.set(xBox2.getRawAxis(5)*0.7);
//		spinMotorRight.set(xBox2.getRawAxis(5)*0.7);
		frontStiltLeft.set (xBox2.getRawAxis(5)*0.7);
		
		// DRIVE BACK STILT - left joystuck
//		backStiltMotor.set(-xBox2.getRawAxis(1)*0.7); 
		backStilt.set(xBox2.getRawAxis(1)*0.7);
		backStiltLeft.set (xBox2.getRawAxis(1)*0.7);

//		frontStilt.set(-xBox.getRawAxis(1)*.35); //front stilts will drop
//		backStilt.set(-xBox.getRawAxis(1)); //front stilts will drop
		backStiltMotor.set(-xBox.getRawAxis(1)); //front stilts will drop

	} // End of testPeriodic
	

	/**
	 * This function is called periodically during Teleop mode
	 */

	@Override
	public void teleopInit() {

		backEncoderCount = 0;
		backLeftEncoderCount = 0;
		frontEncoderCount = 0;
		frontLeftEncoderCount = 0;
		SmartDashboard.putNumber("Back Enc:", backEncoderCount);
		SmartDashboard.putNumber("Front Enc:", frontEncoderCount);
		SmartDashboard.putNumber("B-Left Enc:", backLeftEncoderCount);
		SmartDashboard.putNumber("F-Left Enc:", frontLeftEncoderCount);
		temp = 0;
		finalStateCount = 0;
		onetime = true;
		errorCount = 0;
		errorBLCount = 0;
		errorFLCount = 0;
		backStiltPower = -0.75;
		frontStiltPower = -0.75;
		backStiltLeftPower = -0.75;
		frontStiltLeftPower = -0.75;

		teleopAuton = false;
		teleopState = TELEOP_STATE_1;

	} // end of TeleopInit ()

	@Override
	public void teleopPeriodic() {

			
		SmartDashboard.putData ("Front Sensor:", FrontWheelDistanceSensor);
		SmartDashboard.putData ("Rear Sensor:", RearWheelDistanceSensor);
		backEncoderCount = backStiltEncoder.get();
		backLeftEncoderCount = backStiltLeftEncoder.get();
		frontEncoderCount = frontStiltEncoder.get();
		frontLeftEncoderCount = frontStiltLeftEncoder.get();
		SmartDashboard.putNumber("Back Enc:", backEncoderCount);
		SmartDashboard.putNumber("Front Enc:", frontEncoderCount);
		SmartDashboard.putNumber("B-Left Enc:", backLeftEncoderCount);
		SmartDashboard.putNumber("F-Left Enc:", frontLeftEncoderCount);
		SmartDashboard.putNumber("Back St Pwr:", backStiltPower);
		SmartDashboard.putNumber("Front St Pwr:", frontStiltPower);

		// 
		// Code below is to back Buzz onto platform at end of game
		//
		if (teleopAuton == true)
		{
			switch (teleopState) {
				case TELEOP_STATE_1 :
				{
					// back right Stilt Motor is Master
					//
					// back left Stilt Motor is Slave
					// front right Stilt Motor is Slave
					// front left Stilt Motor is Slave
					//
					// count of 1000 is about 7"
					// count of 2000 is about 14"
					// count of 2850 is about 20"
					if (backEncoderCount < 1250)
					{
						// Master Motor is never changed
//						spinMotorRight.set(backStiltPower);
						backStilt.set(backStiltPower);

						// Slave Motors are changed based on error
//						spinMotorLeft.set(frontStiltPower);
						frontStilt.set(frontStiltPower);
						backStiltLeft.set (backStiltLeftPower);
						frontStiltLeft.set (frontStiltLeftPower);

						SmartDashboard.putNumber("Back St Pwr:", backStiltPower);
						SmartDashboard.putNumber("Front St Pwr:", frontStiltPower);
						SmartDashboard.putNumber("B-Left St Pwr:", backStiltLeftPower);
						SmartDashboard.putNumber("F-Left St Pwr:", frontStiltLeftPower);

						// Calculate error:
						// Error = Master Encoder Count - Slave Encoder Count
						errorCount = backEncoderCount - frontEncoderCount;
					SmartDashboard.putNumber("FError:", errorCount);
						// Error = Master Encoder Count - Slave Encoder Count
						errorBLCount = backEncoderCount - backLeftEncoderCount;
					SmartDashboard.putNumber("BLError:", errorBLCount);
						// Error = Master Encoder Count - Slave Encoder Count
						errorFLCount = backEncoderCount - frontLeftEncoderCount;
					SmartDashboard.putNumber("FLError:", errorFLCount);

						// Update Slave Motor power based on Error and Kp value
						// Note -- we are subtracting here because we are setting
						// power to negative to drive stilts down.  This may change 
						// when we go to Stilt Motor Controllers.
						frontStiltPower = frontStiltPower - (errorCount * 0.001);
						// Limit Stilt power to be greater than -1 and less than 1
						if (frontStiltPower < -1)
						{
							frontStiltPower = -1;
						}
						else
						{
							if (frontStiltPower > 1)
							{
								frontStiltPower = 1;
							}
						}

						// Update Slave Motor power based on Error and Kp value
						// Note -- we are subtracting here because we are setting
						// power to negative to drive stilts down.  This may change 
						// when we go to Stilt Motor Controllers.
						backStiltLeftPower = backStiltLeftPower - (errorBLCount * 0.001);
						// Limit Stilt power to be greater than -1 and less than 1
						if (backStiltLeftPower < -1)
						{
							backStiltLeftPower = -1;
						}
						else
						{
							if (backStiltLeftPower > 1)
							{
								backStiltLeftPower = 1;
							}
						}

						// Update Slave Motor power based on Error and Kp value
						// Note -- we are subtracting here because we are setting
						// power to negative to drive stilts down.  This may change 
						// when we go to Stilt Motor Controllers.
						frontStiltLeftPower = frontStiltLeftPower - (errorFLCount * 0.001);
						// Limit Stilt power to be greater than -1 and less than 1
						if (frontStiltLeftPower < -1)
						{
							frontStiltLeftPower = -1;
						}
						else
						{
							if (frontStiltLeftPower > 1)
							{
								frontStiltLeftPower = 1;
							}
						}
					} // if (backEncoderCount < 2850)
					else
					{	
						if (onetime)
						{
							double power;
							onetime = false;
							// Turn off Master and get Slave as close as possible
//							spinMotorRight.set(0);
							backStilt.set(0);
							errorCount = backEncoderCount - frontEncoderCount;
//Debug						SmartDashboard.putNumber("FError:", errorCount);
							if (errorCount > 0)
							{
								power = -0.5;
							}
							else
							{
								power = 0.5;
							}
//							spinMotorLeft.set(power);
							frontStilt.set(power);
							if (errorBLCount > 0)
							{
								power = -0.5;
							}
							else
							{
								power = 0.5;
							}
							backStiltLeft.set(power);
							if (errorFLCount > 0)
							{
								power = -0.5;
							}
							else
							{
								power = 0.5;
							}
							frontStiltLeft.set(power);
						}
						else
						{
//							spinMotorLeft.set(-0.1);
							frontStilt.set(-0.1);
							backStiltLeft.set (-0.1);
							frontStiltLeft.set (-0.1);
							teleopState = TELEOP_STATE_2;
						}
					} // else (backEncoderCount < 2850)
					break;
				}
				case TELEOP_STATE_2 : 
				{
					// This assume we are "backing" onto the platform
					// So, drving backwards
					// Start driving until sensor detects the platform underneath
					//
		SmartDashboard.putNumber("Back Enc:", backEncoderCount);
		SmartDashboard.putNumber("Front Enc:", frontEncoderCount);
		SmartDashboard.putNumber("B-Left Enc:", backLeftEncoderCount);
		SmartDashboard.putNumber("F-Left Enc:", frontLeftEncoderCount);
					backStiltMotor.set (-0.41); 
					teleopState = TELEOP_STATE_3;
					break;
				}
				case TELEOP_STATE_3 :
				{
					// Check Rear Wheel sensor; Stop when distance is less than 
					temp = RearWheelDistanceSensor.getVoltage();
					SmartDashboard.putNumber("Temp:", temp);
					if (temp > 1.70  || (manualOverride == true))
					{ 
						// Stop Buzz moving backwards on Back Stilt Motor
						backStiltMotor.set (0); 
						teleopState = TELEOP_STATE_4;
					}
					break;
				}
				case TELEOP_STATE_4 : 
				{
					// Raise Back Stilts
//					spinMotorRight.set (0.7);
					backStilt.set (0.7);
					backStiltLeft.set (0.7);
					backEncoderCount = backStiltEncoder.get();
					System.out.print("backEncoderCount: ");
					System.out.println(backEncoderCount);
					backLeftEncoderCount = backStiltLeftEncoder.get();
					SmartDashboard.putNumber("Back Enc:", backEncoderCount);
//Debug		SmartDashboard.putNumber("Front Enc:", frontEncoderCount);
					SmartDashboard.putNumber("B-Left Enc:", backLeftEncoderCount);
//Debug		SmartDashboard.putNumber("F-Left Enc:", frontLeftEncoderCount);
					System.out.print("backLeftEncoderCount: ");
					System.out.println(backLeftEncoderCount);
					if (backEncoderCount < 100) 
					{
						System.out.println("backEncoderCount < 100");
//						spinMotorRight.set(0);
						backStilt.set(0);
						if (backLeftEncoderCount < 100)						{
							System.out.println("backLeftEncoderCount < 100");
							backStiltLeft.set(0);
							teleopState = TELEOP_STATE_5;
						}
					}
					break;
				}
		
				case TELEOP_STATE_5 :
				{
					// Use drive motors to move Buzz back to front sensor
					drive.arcadeDrive(-0.54,0); //drive backward
					//if sensor indicates that back legs are off ledge then do the following
					temp = FrontWheelDistanceSensor.getVoltage();
					SmartDashboard.putNumber("Temp:", temp);
					if (temp > 1.7 || manualOverride2 == true)
					{
						// Stop Buzz moving backwards on the Drive Motors
						drive.arcadeDrive(0,0); 
						teleopState = TELEOP_STATE_6;
					}
					break;
				}

				case TELEOP_STATE_6 :
				{
					// Raise Front Stilts
//					spinMotorLeft.set(0.7);
					frontStilt.set(0.7);
					frontStiltLeft.set (0.7);
					frontEncoderCount = frontStiltEncoder.get();
					frontLeftEncoderCount = frontStiltLeftEncoder.get();
					if (frontEncoderCount < 100) 
					{
//						spinMotorLeft.set(0);
						frontStilt.set(0.0);
						if (frontLeftEncoderCount < 100)						{
							frontStiltLeft.set(0);
							teleopState = TELEOP_STATE_7;
						}
					}
					break;
				}
				case TELEOP_STATE_7 :
				{
					drive.arcadeDrive(-0.55,0);
					finalStateCount = finalStateCount + 1;
					if (finalStateCount > 25) //0.5 s ?
					{
						drive.arcadeDrive(0,0);
						teleopState = TELEOP_STATE_8;
					}
					break;
				}
			
				case TELEOP_STATE_8 :
				{
					drive.arcadeDrive(0,0); //stop
					break;
				}
				
			} // End of Switch
		} // if (teleopAuton = true)
		else
		{ // Else(teleopAuton == false)	
			//controller 1 //fix drive!
			// DRIVE - right joystick
			drive.arcadeDrive(xBox.getRawAxis(5)*-1,xBox.getRawAxis(4)*1); 

			// ELEVATOR - left joystick 
			//climb.set(-xBox.getRawAxis(1)); 

			// FRONT STILTS DOWN - top right bumper button
		//	if(xBox.getBumper(Hand.kLeft))
		//	{
    	//		System.out.println("Top Right Bumper Button");
    	//		backStilt.set(-1);
		//	}

			// Back STILTS UP - top right trigger		
		//	backStilt.set((xBox2.getRawAxis(5)*1)); 
		//	backStiltLeft.set((xBox2.getRawAxis(5)*1)); 

			// Front STILTS DOWN - top left bumper button
		//	if(xBox.getBumper(Hand.kRight)) 
		//	{
	    //		frontStilt.set(-1);	
		//	}
			// Front STILTS UP - top left trigger
		//	frontStilt.set((xBox.getRawAxis(5)*1)); 
		//	frontStiltLeft.set((xBox.getRawAxis(5)*1)); 
	
			SmartDashboard.putNumber("Back Enc:", backEncoderCount);
			SmartDashboard.putNumber("Front Enc:", frontEncoderCount);
			SmartDashboard.putNumber("B-Left Enc:", backLeftEncoderCount);
			SmartDashboard.putNumber("F-Left Enc:", frontLeftEncoderCount);
				
			//controller 2
		
			// INTAKE - right joystick
			spinMotorLeft.set(xBox2.getRawAxis(5)*-0.7); // Used for both Walter and Buzz?
			spinMotorRight.set(xBox2.getRawAxis(5)*-0.7); // Comment when testing with Walter

//			//elevator
//			climb.set((xBox2.getTriggerAxis(Hand.kLeft)*-0.5));
//
//			climb.set((xBox2.getTriggerAxis(Hand.kRight)*0.5));
			// ELEVATOR - left joystick 
			climb.set(-xBox2.getRawAxis(1)); 
		
		
			// DRIVE BACK STILT - left joystuck
//		backStiltMotor.set(-xBox2.getRawAxis(1)*0.7);  // Comment out when testing with Walter
//		spinMotorRight.set(xBox2.getRawAxis(1)*0.7); // Comment out with Buzz

//		if(xBox.getYButton())
//		{
//			FrontSolenoid.set (DoubleSolenoid.Value.kForward);
//		}
//		else
//		{
//			FrontSolenoid.set (DoubleSolenoid.Value.kReverse);
//		}
				
			// Back Button Logic:
			//
			//  If Back Button is pushed, lift Big Tom
			//  If Back Button is pushed, Back Buzz onto platform
			//
			if(xBox.getBackButton())
			{
		    	// Lift Big Tom
//		    BigTom.set(DoubleSolenoid.Value.kReverse);
    			System.out.println("Big Tom Climbs");
				// when button is pushed, we allow Teleop Autonomous to back Buzz
				// on to platform at end of game.
				teleopAuton = true;
			}//if(xBox.getBackButton())

			// Start Button Logic:
			//
			//  If Start Button is pushed, lower Big Tom
			//  If Back Button is pushed, stop Buzz from going onto platform
			//

		}// Else(teleopAuton ==false)

		if(xBox.getStartButton())
		{
			manualOverride = true;
		}
		if(xBox2.getStartButton())
		{
			manualOverride2 = true;
		}

	} // TeleopPeriodic

	private double abs(double tempX) {
		// TODO Auto-generated method stub
		return 0; 
	}
}

