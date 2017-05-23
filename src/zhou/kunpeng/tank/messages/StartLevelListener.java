package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.NetListener;
import zhou.kunpeng.tank.states.PrepareLevelState;

/**
 * Created by JA on 2017/5/23.
 */
public class StartLevelListener implements NetListener {

    private PrepareLevelState state;

    public StartLevelListener(PrepareLevelState prepareLevelState) {
        state = prepareLevelState;
    }

    @Override
    public boolean tryInterpret(String line) {
        if (!line.startsWith("levelstart"))
            return false;

        state.partnerStarts();
        return true;
    }
}
