package zhou.kunpeng.tank;

/**
 * Created by JA on 2017/5/21.
 * <p>
 * This class is for counting scores.
 * It seems a little confusing to use Class as key. Use non-dynamic properties instead!
 * </p>
 */
public class PlayerState {

    public static final int INIT_LIFE = 4;

    public int life = INIT_LIFE;

    public int normalTankCount = 0;
    public int secondaryTankCount = 0;
    public int mobileTankCount = 0;
    public int toughTankCount = 0;

    public static final int NORMAL_TANK_SCORE = 100;
    public static final int SECONDARY_TANK_SCORE = 200;
    public static final int MOBILE_TANK_SCORE = 300;
    public static final int TOUGH_TANK_SCORE = 300;

}
