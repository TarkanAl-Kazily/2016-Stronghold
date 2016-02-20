package org.usfirst.frc.team4915.stronghold.commands.IntakeLauncher;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LaunchBallCommandGroup extends CommandGroup {

    public final int WAIT_DURATION = 500;

    public LaunchBallCommandGroup() {
        addSequential(new SpinLaunchWheelsOutCommand());
        addSequential(new ActivateLauncherPneumaticCommand());
        addSequential(new RetractLauncherPneumaticCommand());
        addSequential(new StopWheelsCommand());
    }
}
