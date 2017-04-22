package fitple.duksung.ac.kr.fitple2;

/**
 * Created by YEJIN on 2016-09-13.
 */
public interface RequestCode {
    final int LIKE_REQUEST = 100;
    public static final int PICK_FROM_CAMERA = 0;
    public static final int PICK_FROM_ALBUM=1;
    public static final int CROP_FROM_CAMERA = 2;

    final String LIKE_URL = "LikeServlet";
}
