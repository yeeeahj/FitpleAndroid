package fitple.duksung.ac.kr.fitple2;

/**
 * Created by YEJIN on 2016-09-18.
 */
public class MyInfo {
    private static MyInfo ourInstance = new MyInfo();

    public String email;
    public String pw;
    static public int member_seq;

    public static MyInfo getInstance() {
        return ourInstance;
    }

    private MyInfo() {

    }
}
class GroupInfo{
    public int group_seq;
    public String group_name;
    public String group_image;
    public  GroupInfo (int group_seq,String group_name,String group_image) {
       this.group_seq= group_seq;
        this.group_name= group_name;
        this.group_image= group_image;
    }


}

class FeedInfo{

    private int   feed_seq;
    private int   member_seq;
    private String   date;
    private String   nickname;
    private String   image;
    private String  content;
    private int like_num;
    private boolean   like_st;
    private int   comment_num;

    public FeedInfo(int feed_seq, int member_seq, String  date, String nickname, String image, String content,int like_num, boolean like_st, int comment_num){
        this.feed_seq = feed_seq;
        this.member_seq = member_seq;
        this.date = date;
        this.nickname = nickname;
        this.image = image;
        this.content = content;
        this.like_num = like_num;
        this.like_st = like_st;
        this.comment_num = comment_num;
    }
    String getImage(){
        return image;
    }
    Boolean getLikeState(){
        return like_st;
    }
    String getDate(){
        return date;
    }
    String getContent(){
        return content;
    }
    int getConmment(){
        return comment_num;
    }
    int getFeed_seq(){
        return feed_seq;
    }
    int getMember_seq(){
        return member_seq;
    }
    String getNickname(){
        return nickname;
    }
    int getLikeNum(){ return like_num; }

}
class CommentInfo{
    int member_seq;
    String nickname;
    String image;
    int comment_seq;
    String date;
    String comment;
    public CommentInfo( int member_seq,    String nickname, String image, int comment_seq, String date, String comment){
        this.member_seq= member_seq;
        this.nickname = nickname;
        this.image = image;
        this.comment_seq = comment_seq;
        this.date = date;
        this.comment = comment;

    }
    public String getNickname(){
        return nickname;
    }
    public String getProfile(){
        return image;
    }
    public String getDate(){
        return date;
    }
    public String getComment(){
        return comment;
    }
    public int getMember_seq(){
        return member_seq;
    }
    public int getComment_seq(){
        return comment_seq;
    }
}