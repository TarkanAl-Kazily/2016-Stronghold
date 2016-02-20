package org.usfirst.frc.team4915.stronghold.vision.robot;

import org.usfirst.frc.team4915.stronghold.Robot;
import org.usfirst.frc.team4915.stronghold.commands.IntakeLauncher.AimerGoToAngleCommand;
import org.usfirst.frc.team4915.stronghold.subsystems.DriveTrain;
import org.usfirst.frc.team4915.stronghold.subsystems.IntakeLauncher;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.NamedSendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

/*
 * The VisionState class provides access within the Robot and the
 * driver station to the values produced by the vision system. 
 */

public class VisionState implements NamedSendable {

    private static VisionState s_instance;

    public synchronized static VisionState getInstance() {
        if (s_instance == null)
            s_instance = new VisionState();
        return s_instance;
    }

    public boolean AutoAimEnabled = false;
    public boolean TargetHigh = true;
    public int FPS = 0;
    public int TargetsAcquired = 0;
    public int TargetX = 0;
    public int TargetY = 0;
    public int TargetSize = 0;
    public double TargetResponse = 0;
    public int TargetClass = 0;

    private ITable m_table = null;
    private final ITableListener m_listener = new ITableListener() {

        @Override
        public void valueChanged(ITable table, String key, Object value, boolean isNew) {
            /*
             * All numbers are stored as doubles in the network tables, event
             * those posted as int, float.
             */
            // System.out.println();
            System.out.println(key + " " + value + " " + value.getClass().getName());
            if (key.equals("~TYPE~")) {
                return;
            } else {
                // System.out.println(key + " " + value);
                double num = (Double) value;
                int ival = (int) num;
                if (key.equals("FPS"))
                    s_instance.FPS = ival;
                else if (key.equals("TargetsAcquired"))
                    s_instance.TargetsAcquired = ival;
                else if (key.equals("TargetX"))
                    s_instance.TargetX = ival;
                else if (key.equals("TargetY"))
                    s_instance.TargetY = ival;
                else if (key.equals("TargetSize"))
                    s_instance.TargetSize = ival;
                else if (key.equals("TargetResponse"))
                    s_instance.TargetResponse = num;
                else if (key.equals("TargetClass"))
                    s_instance.TargetClass = ival;
            }
        }
    };

    /** constructor is private, we're singleton for now */
    private VisionState() {
    }

    public String getName() {
        return "Vision";
    }

    public ITable getTable() {
        return m_table;
    }

    public String getSmartDashboardType() {
        return "Vision";
    }

    public void initTable(ITable subtable) {
        if (this.m_table != null)
            this.m_table.removeTableListener(m_listener);
        this.m_table = subtable;
        m_table.addTableListenerEx(m_listener, ITable.NOTIFY_NEW | ITable.NOTIFY_IMMEDIATE);

        this.AutoAimEnabled = m_table.getBoolean("AutoAimEnabled", false);
        this.TargetHigh = m_table.getBoolean("TargetHigh", true);
        this.FPS = (int) m_table.getNumber("FPS", 0.);
        this.TargetsAcquired = (int) m_table.getNumber("TargetsAcquired", 0);
        this.TargetX = (int) m_table.getNumber("TargetX", 0);
        this.TargetY = (int) m_table.getNumber("TargetY", 0);
        this.TargetSize = (int) m_table.getNumber("TargetSize", 0);
        this.TargetResponse = m_table.getNumber("TargetResponse", 0.);
        this.TargetClass = (int) m_table.getNumber("TargetClass", 0);

    }
    
    public void toggleAimState(boolean toggleEnable, boolean toggleTarget) {
    	if(toggleEnable) {
    		this.AutoAimEnabled = !this.AutoAimEnabled;
    		m_table.putBoolean("AutoAimEnabled", this.AutoAimEnabled);
    		System.out.println("AutoAimEnabled:" + this.AutoAimEnabled);
    	}
    	if(toggleTarget) {
    		this.TargetHigh = !this.TargetHigh;
    		//m_table.putBoolean("TargetHigh", this.TargetHigh);
    		System.out.println("TargetHigh:" + this.TargetHigh);
    	}
    }
    
    public boolean followTargetX(DriveTrain driveTrain) {
    	if(this.AutoAimEnabled && this.TargetsAcquired > 0) {
    		if (this.TargetX <= -1){
    			driveTrain.turn(false);
    		}
    		else {
    			driveTrain.turn(true);
    		}
    		return true;
    	}
    	return false;
    }
    
    public boolean followTargetY(IntakeLauncher intakeLauncher) {
    	if(this.AutoAimEnabled && this.TargetsAcquired > 0) {
    		intakeLauncher.setPointInDegrees(TargetY);
    		return true;
    	}
    	else {
    		return false;
    	}
    }
}
