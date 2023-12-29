package frc.robot;

import java.util.Map;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.DifferentialDrive.WheelSpeeds;
import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class ShuffleboardData {
    private ShuffleboardTab tab = Shuffleboard.getTab("TheaterBot");
    private SendableChooser<String> drive_mode = new SendableChooser<>();

    // private ShuffleboardLayout graph_layout = tab.getLayout("Arm Speeds", plot)
    
    // basic entries
    public GenericEntry armA_speed = tab.add("Arm A Speed", 0)
                                        .withWidget(BuiltInWidgets.kNumberBar)
                                        .withPosition(3, 0)
                                        .withSize(1,1)
                                        .getEntry();
    public GenericEntry armB_speed = tab.add("Arm B Speed", 0)
                                        .withWidget(BuiltInWidgets.kNumberBar)
                                        .withSize(1,1)
                                        .withPosition(4, 0)
                                        .getEntry();
    public GenericEntry armX_speed = tab.add("Arm X Speed", 0)
                                        .withWidget(BuiltInWidgets.kNumberBar)
                                        .withSize(1,1)
                                        .withPosition(3, 1)
                                        .getEntry();
    public GenericEntry armY_speed = tab.add("Arm Y Speed", 0)
                                        .withWidget(BuiltInWidgets.kNumberBar)
                                        .withSize(1,1)
                                        .withPosition(4, 1)
                                        .getEntry();

    public GenericEntry armA_on = tab.add("Arm A", false)
                                        .withPosition(0, 1)
                                        .getEntry();
    public GenericEntry armB_on = tab.add("Arm B", false)
                                        .withPosition(1, 1)
                                        .getEntry();
    public GenericEntry armX_on = tab.add("Arm X", false)
                                        .withPosition(0, 0)
                                        .getEntry();
    public GenericEntry armY_on = tab.add("Arm Y", false)
                                        .withPosition(1, 0)
                                        .getEntry();
    public GenericEntry eye_on = tab.add("Eye", false)
                                        .withPosition(0, 2)
                                        .getEntry();
    public GenericEntry voltage = tab.add("Battery Voltage", 0)
                                        .withWidget(BuiltInWidgets.kVoltageView)
                                        .withProperties(Map.of("Orientation", "VERTICAL", "Number of tick marks", 14, "Max", 13))
                                        .withSize(1, 3)
                                        .withPosition(2,0)
                                        .getEntry();

    // persistent entries
    public GenericEntry eye_speed = tab.addPersistent("Eye Speed", 0.1)
                                        .withPosition(1, 2)
                                        .withSize(1, 1)
                                        .withWidget(BuiltInWidgets.kNumberSlider)
                                        .withProperties(Map.of("Min",0,"Max",1))
                                        .getEntry();
    public GenericEntry deadband_zone = tab.addPersistent("Deadband Zone", 0.05)
                                        .withPosition(5, 0)
                                        .withSize(2, 1)
                                        .withWidget(BuiltInWidgets.kNumberSlider)
                                        .withProperties(Map.of("Min",0,"Max",0.5))
                                        .getEntry();
    public GenericEntry max_speed = tab.addPersistent("Max Speed", 0.8)
                                        .withPosition(5, 1)
                                        .withSize(2, 1)
                                        .withWidget(BuiltInWidgets.kNumberSlider)
                                        .withProperties(Map.of("Min",0,"Max",1))
                                        .getEntry();
    
    private DifferentialDriveOdometry m_odometry = new DifferentialDriveOdometry(
        new Rotation2d(0),
        0.0,0.0,
        new Pose2d(0, 0, new Rotation2d())
    );
    private final Field2d m_field = new Field2d();
    private double l_encoder = 0;
    private double r_encoder = 0;

                                          
    public ShuffleboardData(DifferentialDrive drivebase) {
        SmartDashboard.putData("Field", m_field);
        Shuffleboard.selectTab("TheaterBot");
        tab.add(drivebase).withPosition(7, 0);
        drive_mode.setDefaultOption("Curvature Drive", "curve");
        drive_mode.addOption("Arcade Drive", "arcade");
        drive_mode.addOption("Tank Drive", "tank");
        tab.add("Drive Mode", drive_mode).withPosition(5, 2).withSize(2,1);
    }
    public String getDriveMode() {
        return drive_mode.getSelected();
    }
    public void setFakeEncoders(WheelSpeeds speeds) {
        l_encoder += speeds.left;
        r_encoder += speeds.right;
        m_odometry.update(new Rotation2d(r_encoder, l_encoder), l_encoder, r_encoder);
        m_field.setRobotPose(m_odometry.getPoseMeters());
    }
}
