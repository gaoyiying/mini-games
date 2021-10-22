package com.swufe.stu.mini_games;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements SurfaceHolder.Callback ,Runnable{

    public static final int GAME_START=200;//游戏开始
    public static final int GAME_STOP=400;//游戏停止
    public static final int GAME_OVER=500;//游戏结束
    public static final int GAME_READY=100;//游戏准备
    private int gameStatus;//游戏状态
    //所诉要的bitmap对象
    private Bitmap bitmap_background_game,bitmap_bird1,bitmap_bird2,bitmap_bird3,bitmap_ground,bitmap_orstacle,bitmap_orstacleHead;

    private Canvas mCanvas;//canvas对象
    private int ViewWidth,ViewHeight;//view的宽高，因为view充满整个屏幕所以也是屏幕的宽高
    private SurfaceHolder surfaceHolder;
    private boolean isDraw = true;//绘制线程开关
    private Body background;//背景图绘制对象
    private Body bird;//小鸟绘制对象
    private Body[] grounds;//地板数组
    private List<Body> obstacles;//障碍物集合
    int obstacleDecm;//障碍物间的间隔
    int Score = 0;//分数

    /*
    private GameListener gameListener;

    public GameListener getGameListener() {
        return gameListener;
    }

    public void setGameListener(GameListener gameListener) {
        this.gameListener = gameListener;
    }

     */

    public GameView(Context context) {
        super(context);
        initView(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context);
        initView(context);
    }

    public GameView(Context context,AttributeSet attrs,int defStyleAttr) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        //初始化bitmap
        bitmap_background_game = BitmapFactory.decodeResource(context.getResources(),R.drawable.background_game);
        bitmap_bird1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.bird1);
        bitmap_bird2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.bird2);
        bitmap_bird3 = BitmapFactory.decodeResource(context.getResources(),R.drawable.bird3);
        bitmap_ground = BitmapFactory.decodeResource(context.getResources(),R.drawable.ground);
        bitmap_orstacle = BitmapFactory.decodeResource(context.getResources(),R.drawable.obstacle);
        bitmap_orstacleHead = BitmapFactory.decodeResource(context.getResources(),R.drawable.obstacle_head);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }


    private void initData() {
        gameStatus = GAME_READY;
        ViewWidth = getMeasuredWidth();
        ViewHeight = getMeasuredHeight();
        bitmap_background_game = getRationBitmap(bitmap_background_game,(float) ViewWidth/bitmap_background_game.getWidth(),
                (float) ViewHeight/bitmap_background_game.getHeight());//获取缩放之后的背景图bitmap缩放比例为屏幕宽高，原图片宽高（全屏）
        bitmap_bird1 = getRationBitmap(bitmap_bird1,0.65f,0.65f);//小鸟图片缩放0.65
        bitmap_bird2 = getRationBitmap(bitmap_bird2,0.65f,0.65f);//小鸟图片缩放0.65
        bitmap_bird3 = getRationBitmap(bitmap_bird3,0.65f,0.65f);//小鸟图片缩放0.65
        //初始化绘制对象
        background = new Body(bitmap_background_game,0,0,bitmap_background_game.getWidth(),bitmap_background_game.getHeight());
        bird = new Body(bitmap_bird1, ViewWidth/2-bitmap_bird1.getWidth()/2,
                ViewHeight/2,bitmap_bird1.getWidth(),bitmap_bird1.getHeight());//小鸟初始坐标在屏幕宗信，x:中心一小鸟鸟宽/2
        grounds = createGrounds();
        obstacles = new ArrayList<>();//障碍物集合
        obstacleDecm = bird.getH()*6;//管道间隔为小鸟高度的6倍
        new Thread(this).start();//开启控制线程
    }

    public void reSet(){
        BirdSpinData = 0;
        CreateObstacleTime = 0;
        upStartTime = 0;
        bird.setX(ViewWidth/2-bitmap_bird1.getWidth()/2);
        bird.setY(ViewHeight/2);
        obstacles = new ArrayList<>();
        isDown = false;
        changeBitmapTime=0;
        //Score = 0;
        gameStatus = GAME_READY;
        /*
        if(gameListener!=null) {
            gameListener.gameReady();
        }

         */
    }

    private Body[] createGrounds() {
        //y坐标 屏幕高度-地板图片高度
        Body body = new Body(bitmap_ground,0,ViewHeight-bitmap_ground.getHeight(),bitmap_ground.getWidth(),bitmap_ground.getHeight());
        Body body1 = new Body(bitmap_ground,body.getX() + body.getW(),ViewHeight-bitmap_ground.getHeight(),bitmap_ground.getWidth(),bitmap_ground.getHeight());
        return new Body[]{body,body1};
    }

    public Bitmap getRationBitmap(Bitmap bitmap,float dx,float dy){//获取dx、dy缩放比例后的bitmap
        return Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*dx),(int)(bitmap.getHeight()*dy),true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        initData();
    }


    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        //销毁时关闭线程
        isDraw = false;
    }

    @Override
    public void run() {
        while (isDraw){
            drawMain();
        }
    }

    private void drawMain() {//这里绘制整个游戏
        //先获取Canvas
        mCanvas = surfaceHolder.lockCanvas();
        birdMove();//小鸟动作//三张图片切换
        if(gameStatus == GAME_START){//游戏开始时才有动作
            downBird();//小鸟下落与跳跃
            moveGround();//地板移动
            createObstacle();//生成障碍物
            moveObstacle();//障碍物移动
        }
        if(gameStatus == GAME_STOP){
            overAnim();//小鸟死亡动画
        }
        drawBackground();//先绘制背景图
        drawBird();//绘制小鸟
        drawGrounds();//绘制地板
        drawObstacle();//绘制障碍物
        checkOver();//检测失败
        surfaceHolder.unlockCanvasAndPost(mCanvas);//绘制完成后提交
    }

    private void overAnim() {
        //判断小鸟的中心点在障碍物中心点的左侧还是右侧
        BirdSpinData+=30;
        if(bird.getX() + bird.getW()/2 > collsionBody.getX() + collsionBody.getW()/2){//代表障碍物的右侧发生碰撞
            if(bird.getX() < collsionBody.getX() + collsionBody.getW()){
                bird.setX(bird.getX() + 5);
                bird.setY(bird.getY() - 15);
                return;
            }
            bird.setY(bird.getY() + 15);
            bird.setX(bird.getX() + 5);
        }else {//左侧发生碰撞
            if(bird.getX() + bird.getW() > collsionBody.getX()){
                bird.setX(bird.getX() - 5);
                bird.setY(bird.getY() - 15);
                return;
            }
            bird.setY(bird.getY() + 15);
            bird.setX(bird.getX() - 5);
        }
    }

    Body collsionBody;

    private void checkOver() {//
        //检测是否落地
        if(bird.getY()+bird.getH() >= grounds[0].getY()){
            //代表落地
            gameStatus = GAME_OVER;//游戏结束
            /*
            if(gameListener!=null){
                gameListener.gameOver();
            }

             */
        }else {
            //检测是否碰撞
            for(Body body:obstacles){
                if(CollsionRect(bird.getX(),bird.getY(),bird.getW(),bird.getH()
                                ,body.getX(),body.getY(),body.getW(),body.getH())){
                    //碰撞了
                    gameStatus = GAME_STOP;//游戏停止，播放死亡动画，然后结束
                    collsionBody = body;//记录一下碰撞的障碍物
                }
            }
        }
    }

    //碰撞检测 两个矩形
    public boolean CollsionRect(int x1,int y1,int w1,int h1,int x2,int y2,int w2,int h2){
        if(x1 >= x2 && x1 >= x2+w2){//在右侧不碰撞
            return false;
        }else if(x1 <= x2 && x1+w1 <= x2){//左侧不碰撞
            return false;
        }else if(y1 >= y2 && y1 >= y2+h2){//上侧不碰撞
            return false;
        }else if(y1 <= y2 && y1+h1 <= y2){
            return false;
        }
        //当不满足所有不碰撞条件时，就是碰撞了
        return true;
    }

    private void moveObstacle() {
        List<Body> bodies = new ArrayList<>();
        for(Body body:obstacles){
            body.setX(body.getX() - moveSpeed);//移动速度与地板相同
            if(body.getX()+body.getW()+10 < 0){
                //代表移除屏幕了
                bodies.add(body);
            }
            //记录分数
            if(!body.isScore && body.getX()+body.getW()/2 < bird.getX()){
                //分数+1
                Score++;
                /*
                if(gameListener!=null){
                    gameListener.addScore(Score);
                    body.setScore(true);
                }

                 */
            }
        }
        //删除移除屏幕的障碍物
        obstacles.removeAll(bodies);
    }

    int CreateObstacleTime = 0;//生成障碍物时间
    private void createObstacle() {
        CreateObstacleTime++;
        if(CreateObstacleTime >= 80){
            CreateObstacleTime = 0;
            //每80帧生成2个障碍物 障碍物高度随机 最小值100 最大值地板坐标-管道距离-100
            int ranH = 100 + (int) (Math.random()*(grounds[0].getY() - obstacleDecm - 100));
            //每次管道生成X起始坐标为屏幕宽度+50
            //
            Body body = new Body(getObstacleBitmap(ranH,true),ViewWidth+10,0,bird.getW()*2,ranH);
            Bitmap bitmap = getObstacleBitmap(grounds[0].getY() - obstacleDecm - body.getH(),false);
            Body body1 = new Body(bitmap,ViewWidth+10,body.getH()+obstacleDecm,bird.getW()*2,bitmap.getHeight());
            obstacles.add(body);
            obstacles.add(body1);
        }
    }
    public Bitmap getObstacleBitmap(int h,boolean isRot){//生成障碍物图片 h为图片高度 is为是否旋转
        //创建一个新的图片
        Bitmap bitmap = Bitmap.createBitmap(bird.getW()*2,h, Bitmap.Config.ARGB_8888);//管道宽度为小鸟的2倍 高度随机
        Canvas canvas = new Canvas(bitmap);
        if(isRot){
            Matrix matrix = new Matrix();
            matrix.setRotate(180,bird.getW()*2/2,h/2);//旋转
            canvas.setMatrix(matrix);
        }
        //先绘制管道的身体部位
        Bitmap ObstacleBody = getRationBitmap(bitmap_orstacle,(float) (bird.getW() * 2 - 30)/bitmap_orstacle.getWidth(),//管道身体细一点
                (float) h/bitmap_orstacle.getHeight());//缩放后管道身体的图片
        Matrix matrix1 = new Matrix();
        matrix1.setTranslate(15,0);
        canvas.drawBitmap(ObstacleBody,matrix1,null);

        //再绘制管道头部
        Bitmap obstacleHead = getRationBitmap(bitmap_orstacleHead,(float) bird.getW()*2/bitmap_orstacleHead.getWidth(),
                (float) bird.getH()/bitmap_orstacleHead.getHeight());//管道头部为小鸟高度的两倍
        canvas.drawBitmap(obstacleHead,new Matrix(),null);

        return bitmap;
    }

    private void drawObstacle() {
        for(Body body:obstacles){
            mCanvas.drawBitmap(body.getBitmap(),body.getX(),body.getY(),null);//绘制障碍物
        }
    }

    int moveSpeed = 8;//每一帧移动8
    private void moveGround() {
        if(grounds[0].getX()+grounds[0].getW() >0){//没有移除屏幕
            grounds[0].setX(grounds[0].getX() - moveSpeed);
        }else{
            grounds[0].setX(grounds[1].getX()+grounds[1].getW() - 20);
        }
        if(grounds[1].getX()+grounds[1].getW() >0){//没有移除屏幕
            grounds[1].setX(grounds[1].getX() - moveSpeed);
        }else{
            grounds[1].setX(grounds[0].getX()+grounds[0].getW() - 20);
        }
    }

    private void drawGrounds() {
        mCanvas.drawBitmap(grounds[0].getBitmap(),grounds[0].getX(),grounds[0].getY(),null);
        mCanvas.drawBitmap(grounds[1].getBitmap(),grounds[1].getX(),grounds[1].getY(),null);
    }

    boolean isDown = true;//跳跃/下落开关
    int downSpeed;//下落速度 每一帧下落的坐标
    int upStartTime = 0;//已经跳跃时间
    int upTime = 20;//需要跳跃时间
    int upSpeed = 12;//每一帧跳跃距离
    private void downBird() {
        if(isDown){
            //下落时旋转
            BirdSpinData = 45;
            bird.setY(bird.getY()+(downSpeed++));//速度渐增 递增
        }else{
            //定义跳跃动作
            if(upTime - upStartTime >=0){
                BirdSpinData = -45;//向上旋转45
                bird.setY(bird.getY() - upSpeed);//每一帧Y-12
                upStartTime++;
            }else{//代表跳跃结束
                isDown = true;
                upStartTime = 0;
                downSpeed = 0;
            }
        }
    }

    int birdBitmapData = 1;//切换图片控制
    int changeBitmapTime = 0;
    private void birdMove() {
        changeBitmapTime++;
        if(changeBitmapTime >= 10){//每10帧改变一次图片
            switch(birdBitmapData){
                case 1:
                    bird.setBitmap(bitmap_bird1);
                    break;
                case 2:
                    bird.setBitmap(bitmap_bird2);
                    break;
                case 3:
                    bird.setBitmap(bitmap_bird3);
                    break;
            }
            changeBitmapTime = 0;
            birdBitmapData = birdBitmapData==3?1:birdBitmapData+1;
        }
    }

    int BirdSpinData = 0;//小鸟旋转角度
    private void drawBird() {
        //因为小鸟有旋转、平移等动作，这里用矩阵对象
        Matrix matrix = new Matrix();
        matrix.setTranslate(bird.getX(),bird.getY());
        matrix.postRotate(BirdSpinData,bird.getX()+bird.getW()/2,bird.getY()+bird.getH()/2);//定义旋转点 小鸟的中心点
        mCanvas.drawBitmap(bird.getBitmap(),matrix,null);
    }

    private void drawBackground() {
        mCanvas.drawBitmap(background.getBitmap(),background.getX(),background.getY(),null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN://手指按下时
                if(gameStatus == GAME_READY){
                    gameStatus = GAME_START;
                }else if(gameStatus == GAME_START){
                    isDown = false;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /*
    public interface GameListener{//与activity交互的接口
        void addScore(int score);
        void gameOver();
        void gameReady();
    }

     */

    class Body{//定义一个包含基础属性的类 绘制成员类的基础属性
        Bitmap bitmap;
        int x,y,w,h;//xy坐标 宽高
        boolean isScore = false;//是否已经计分 //仅障碍物用到

        public boolean isScore() {
            return isScore;
        }

        public void setScore(boolean score) {
            isScore = score;
        }

        public Body(Bitmap bitmap, int x, int y, int w, int h){
            this.bitmap = bitmap;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getW() {
            return w;
        }

        public void setW(int w) {
            this.w = w;
        }

        public int getH() {
            return h;
        }

        public void setH(int h) {
            this.h = h;
        }
    }
}
