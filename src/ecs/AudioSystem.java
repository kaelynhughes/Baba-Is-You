package ecs;

import edu.usu.audio.Sound;
import edu.usu.audio.SoundManager;

public class AudioSystem {
    private static AudioSystem system;
    private final SoundManager manager;

    private final Sound audioDeath;
    private final Sound audioMove;
    private final Sound audioRuleChange;
    private final Sound audioWin;

    private AudioSystem() {
        system = this;
        manager = new SoundManager();

        audioDeath = manager.load("Death Sound", "resources/audio/death.ogg", false);
        audioMove = manager.load("Move Sound", "resources/audio/move.ogg", false);
        audioRuleChange = manager.load("Rule Change Sound", "resources/audio/rule_change.ogg", false);
        audioWin = manager.load("Win Sound", "resources/audio/win.ogg", false);
    }

    public static AudioSystem getInstance() {
        if (system == null) {
            system = new AudioSystem();
        }
        return system;
    }

    public void playAudioDeath() {
        audioDeath.play();
    }

    public void playAudioMove() {
        audioMove.play();
    }

    public void playAudioRuleChange() {
        audioRuleChange.play();
    }

    public void playAudioWin() {
        audioWin.play();
    }

    public void cleanup() {
        manager.cleanup();
    }
}
