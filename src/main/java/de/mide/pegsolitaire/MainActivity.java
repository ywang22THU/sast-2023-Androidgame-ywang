package de.mide.pegsolitaire;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static de.mide.pegsolitaire.model.PlaceStatusEnum.SPACE;
import static de.mide.pegsolitaire.model.PlaceStatusEnum.BLOCKED;
import static de.mide.pegsolitaire.model.PlaceStatusEnum.PEG;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Space;
import android.widget.Toast;

import de.mide.pegsolitaire.model.PlaceStatusEnum;
import de.mide.pegsolitaire.model.SpacePosition;
import de.mide.pegsolitaire.model.GameMap;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    public static final String TAG4LOGGING = "PegSolitaire";

    private static final int TEXT_COLOR_BROWN = 0xffa52a2a;
    private static final int TEXT_COLOR_RED = 0xffff0000;

    /**
     * Unicode字符：实心方块
     */
    private static final String TOKEN_MARK = "■";

    /**
     * 储存地图
     */
    private static final GameMap gamemapset = new GameMap();
    /**
     * 用于存储棋盘初始化的数组。
     */
    private PlaceStatusEnum[][] PLACE_INIT_ARRAY = gamemapset.Map1;

    private int _sizeColumn = PLACE_INIT_ARRAY.length;

    private int _sizeRow = PLACE_INIT_ARRAY[0].length;

    /**
     * 用于存储棋盘上的棋子和空位置的数组。
     */
    private PlaceStatusEnum[][] _placeArray = null;

    /**
     * 当前棋盘上的棋子数量。
     */
    private int _numberOfPegs = -1;
    /**
     * 当前执行的步数。
     */
    private int _numberOfSteps = -1;
    /**
     * 选中的棋子是否已经被移动了。
     */
    private boolean _selectedPegMoved = false;

    /**
     * 用于存储棋盘上的棋子的按钮。
     */
    private ViewGroup.LayoutParams _buttonLayoutParams = null;
    /**
     * 储存棋子的被选中否
     */
    private boolean[][] ButtonClicked = new boolean[_sizeColumn][_sizeRow];
    /**
     * 上一个被选中棋子的位置
     */
    private int ColumnOfPreviousButton = -1;
    private int RowOfPreviousButton = -1;

    /**
     * 用于开始新游戏的按钮。
     */
    private Button _startButton = null;

    /**
     * 棋盘上的棋子和空位置的布局。
     */
    private GridLayout _gridLayout = null;

    /**
     * 玩家名字
     */
    private static String name = "";

    /**
     * 地图名字
     */
    private static String map_name = "Map1";
    /**
     * 用于处理点击棋盘上的棋子的事件。
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG4LOGGING, "column=" + _sizeColumn + ", row=" + _sizeRow + "px:");

        _gridLayout = findViewById(R.id.boardGridLayout);

        displayResolutionEvaluate();
        actionBarConfiguration();
        initializeBoard();
    }

    /**
     * 从显示器读取分辨率并将值写入适当的成员变量。
     */
    private void displayResolutionEvaluate() {

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;

        Log.i(TAG4LOGGING, "Display-Resolution: " + displayWidth + "x" + displayHeight);

        int _sideLengthPlace = displayWidth / _sizeColumn;

        _buttonLayoutParams = new ViewGroup.LayoutParams(_sideLengthPlace,
                _sideLengthPlace);
    }

    /**
     * 初始化操作栏。
     */
    private void actionBarConfiguration() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {

            Toast.makeText(this, "没有操作栏", Toast.LENGTH_LONG).show();
            return;
        }

        actionBar.setTitle("单人跳棋");
    }

    /**
     * 从资源文件加载操作栏菜单项。
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu_items, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 处理操作栏菜单项的选择。
     * 在扩展的版本中，你需要加入更多的菜单项。
     *
     * @param item 选择的菜单项
     * @return true: 选择的菜单项被处理了
     * false: 选择的菜单项没有被处理
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_new_game) {

            selectedNewGame();
            return true;

        } else
            return super.onOptionsItemSelected(item);
    }

    /**
     * 处理点击"新游戏"按钮的事件。
     * 弹出对话框，询问用户是否要开始新游戏。
     * 如果用户选择"是"，则初始化棋盘，否则不做任何事情。
     */
    public void selectedNewGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("新游戏");
        builder.setMessage("请确认是否开启一局新游戏");
        builder.setPositiveButton("是", (dialogInterface, i) -> chooseBoard());
        builder.setNegativeButton("否",null);
        AlertDialog dialog = builder.create();
        dialog.show();
        // TODO
    }

    /**
     * 选择地图
     */
    public void chooseBoard(){
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        RadioButton map1 = new RadioButton(this);
        RadioButton map2 = new RadioButton(this);
        RadioButton map3 = new RadioButton(this);
        RadioButton map4 = new RadioButton(this);
        map1.setText("Map1");
        map2.setText("Map2");
        map3.setText("Map3");
        map4.setText("Map4");
        radioGroup.addView(map1);
        radioGroup.addView(map2);
        radioGroup.addView(map3);
        radioGroup.addView(map4);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("地图");
        builder.setMessage("请选择你想要的地图");
        builder.setView(radioGroup);
        builder.setPositiveButton("确定", (dialogInterface, i) -> {
            int radioId = radioGroup.getCheckedRadioButtonId();
            if(radioId == map1.getId()){
                PLACE_INIT_ARRAY = gamemapset.Map1;
            } else if (radioId == map2.getId() ) {
                PLACE_INIT_ARRAY = gamemapset.Map2;
            } else if (radioId == map3.getId()) {
                PLACE_INIT_ARRAY = gamemapset.Map3;
            } else if (radioId == map4.getId()) {
                PLACE_INIT_ARRAY = gamemapset.Map4;
            }
            GetMapName();
            initializeBoard();
        });
        builder.setNegativeButton("取消",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 获得地图名字
     */

    private void GetMapName(){
        if(PLACE_INIT_ARRAY == gamemapset.Map1){
            map_name = "Map1";
        } else if (PLACE_INIT_ARRAY == gamemapset.Map2) {
            map_name = "Map2";
        } else if (PLACE_INIT_ARRAY == gamemapset.Map3) {
            map_name = "Map3";
        } else if (PLACE_INIT_ARRAY == gamemapset.Map4) {
            map_name = "Map4";
        } else {
            map_name = "Map0";
        }
    }
    /**
     * 初始化棋盘上的棋子和空位置。
     */
    private void initializeBoard() {

//        if (_gridLayout.getRowCount() == 0) {
//
//            _gridLayout.setColumnCount(_sizeRow);
//
//        } else { // 清除旧的棋盘
//
//            _gridLayout.removeAllViews();
//        }

        _numberOfSteps = 0;
        _numberOfPegs = 0;
        _selectedPegMoved = false;

        _gridLayout.removeAllViews();

        _sizeColumn = PLACE_INIT_ARRAY.length;
        _sizeRow = PLACE_INIT_ARRAY[0].length;

        displayResolutionEvaluate();

        _gridLayout.setColumnCount(_sizeColumn);
        _gridLayout.setRowCount(_sizeRow);
        _gridLayout.requestLayout();
        ButtonClicked = new boolean[_sizeColumn][_sizeRow];
        _placeArray = new PlaceStatusEnum[_sizeColumn][_sizeRow];

        for (int i = 0; i < _sizeColumn; i++) {

            for (int j = 0; j < _sizeRow; j++) {

                PlaceStatusEnum placeStatus = PLACE_INIT_ARRAY[i][j];

                _placeArray[i][j] = placeStatus;

                ButtonClicked[i][j] = false;

                switch (placeStatus) {

                    case PEG:
                        generateButton(i, j, true);
                        break;

                    case SPACE:
                        generateButton(i, j, false);
                        break;

                    case BLOCKED:
                        Space space = new Space(this); // Dummy-Element
                        _gridLayout.addView(space);
                        break;

                    default:
                        Log.e(TAG4LOGGING, "错误的棋盘状态");

                }
            }
        }

        Log.i(TAG4LOGGING, "棋盘初始化完成");
        updateDisplayStepsNumber();
    }

    /**
     * 生成棋盘上的一个位置。
     * 在基础任务中，棋盘上的棋子直接用字符 TOKEN_MARK 表示。
     * 在扩展任务中，棋盘上的棋子用图片表示。
     */
    private void generateButton(int indexColumn, int indexRow, boolean isPeg) {

        Button button = new Button(this);

        float width = button.getWidth()*0.75f;
        float height = button.getHeight()*.075f;
        button.setWidth((int) width);
        button.setHeight((int) height);
        button.setLayoutParams(_buttonLayoutParams);
        button.setOnClickListener(this);

        SpacePosition pos = new SpacePosition(indexColumn, indexRow);
        button.setTag(pos);
        Drawable peg_init_drawable = getResources().getDrawable(R.drawable.init_peg);
        Drawable space_drawable = getResources().getDrawable(R.drawable.space);

        if(isPeg){
            button.setBackground(peg_init_drawable);
            _numberOfPegs++;
        }
        else{
            button.setBackground(space_drawable);
        }

        _gridLayout.addView(button);
        // TODO
    }


    /**
     * 更新操作栏中的步数显示。
     */
    private void updateDisplayStepsNumber() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (name == null) {
                actionBar.setSubtitle("执行步数：" + _numberOfSteps);
            }
            else {
                SharedPreferences sp = getSharedPreferences("player_info",MODE_PRIVATE);
                int minsteps = sp.getInt(map_name,0);
                actionBar.setSubtitle(name + "历史最佳：" + minsteps +" 执行步数：" + _numberOfSteps);
            }
        }
    }

    /**
     * 处理棋盘上的点击事件。
     * 如果被点击的按钮是一个棋子，那么它将被改变选中状态。
     * 也就是说，如果它之前没有被选中，这个棋子会变为红色，
     * 同时，此前被选中的棋子（如果有）将变为棕色。
     * 或者如果它已经被选中，那么它自己将变为棕色。
     * 如果被点击的按钮是一个空位置，那么试图将被选中的棋子移动到该位置。
     * 如果移动成功，你需要更新棋盘上的棋子和空位置。
     * 如果移动失败，你需要显示一个错误信息。
     *
     * @param view 被点击的按钮
     *

     */
    @Override
    public void onClick(View view) {

        Button clickedButton = (Button) view;

        SpacePosition targetPosition = (SpacePosition) clickedButton.getTag();
        SpacePosition previousPosition = new SpacePosition(ColumnOfPreviousButton,RowOfPreviousButton);
        Button previousbutton = getButtonFromPosition(previousPosition);
        // 获取被点击的按钮的位置
        int indexColumn = targetPosition.getIndexColumn();
        int indexRow = targetPosition.getIndexRow();
        PlaceStatusEnum placeStatus = _placeArray[indexColumn][indexRow];

        switch (placeStatus) {

            case PEG:
                if(previousbutton!=null){
                    if(previousbutton!=clickedButton){
                        ButtonClicked[ColumnOfPreviousButton][RowOfPreviousButton] = false;
                        ButtonClicked[indexColumn][indexRow] = true;
                    }
                    else{
                        ButtonClicked[indexColumn][indexRow] ^= true;
                    }
                    formalchange(ColumnOfPreviousButton,RowOfPreviousButton,previousbutton);
                }
                else{
                    ButtonClicked[indexColumn][indexRow] = true;
                }
                formalchange(indexColumn,indexRow,clickedButton);
                ColumnOfPreviousButton = indexColumn;
                RowOfPreviousButton = indexRow;
                // TODO
                break;

            case SPACE:
                SpacePosition skippedPosition = getSkippedPosition(previousPosition,targetPosition);
                if(skippedPosition!=null){
                    Button skippedButton = getButtonFromPosition(skippedPosition);
                    jumpToPosition(previousbutton,clickedButton,skippedButton);
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("错误信息");
                    builder.setMessage("你执行了一个错误操作！");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                // TODO
                break;

            default:
                Log.e(TAG4LOGGING, "错误的棋盘状态" + placeStatus);
        }
    }

    /**
     * 颜色改变
     */
    private void formalchange(int col,int row,Button pre){

        Drawable peg_init_drawable = getResources().getDrawable(R.drawable.init_peg);
        Drawable peg_clicked_drawable = getResources().getDrawable(R.drawable.clicked_peg);
        if(_placeArray[col][row] == PEG) {
            if (ButtonClicked[col][row]) {
                pre.setBackground(peg_clicked_drawable);
            } else {
                pre.setBackground(peg_init_drawable);
            }
        }
    }
    /**
     * 执行跳跃。仅当确定移动合法时才可以调用该方法。
     * 数组中三个位置的状态，和总棋子数发生变化。
     * 同时，在移动后，你需要检查是否已经结束游戏。
     *
     * @param startButton 被选中的棋子
     * @param targetButton 被选中的空位置
     * @param skippedButton 被跳过的棋子
     *
     */
    private void jumpToPosition(Button startButton, Button targetButton, Button skippedButton) {

        Drawable peg_init_drawable = getResources().getDrawable(R.drawable.init_peg);
        Drawable space_drawable = getResources().getDrawable(R.drawable.space);
        Statuschange(startButton);
        Statuschange(targetButton);
        Statuschange(skippedButton);
        startButton.setBackground(space_drawable);
        skippedButton.setBackground(space_drawable);
        targetButton.setBackground(peg_init_drawable);
        SpacePosition targetButtonPosition = (SpacePosition)targetButton.getTag();
        int tarcol = targetButtonPosition.getIndexColumn();
        int tarrow = targetButtonPosition.getIndexRow();
        ButtonClicked[tarcol][tarrow] = false;

        // TODO

        _numberOfSteps++;
        _numberOfPegs--;
        updateDisplayStepsNumber();
        if (_numberOfPegs == 1) {
            showVictoryDialog();
        } else if (!has_movable_places()) {
            showFailureDialog();
        }
    }
    /**
     * 改变对应位置的属性
     */
    private void Statuschange(Button _button){
        SpacePosition pos = (SpacePosition) _button.getTag();
        int poscol = pos.getIndexColumn();
        int posrow = pos.getIndexRow();
        switch (_placeArray[poscol][posrow]){
            case PEG:
                _placeArray[poscol][posrow] = SPACE;
                break;
            case SPACE:
                _placeArray[poscol][posrow] = PEG;
                break;
        }
    }
    /**
     * 返回位置对应的按钮。
     *
     * @param position 位置
     * @return 按钮
     */
    private Button getButtonFromPosition(SpacePosition position) {

        int index = position.getPlaceIndex(_sizeRow);

        if(position.getIndexColumn()<0){
            return null;
        }
        return (Button) _gridLayout.getChildAt(index);
    }

    /**
     * 显示一个对话框，表明游戏已经胜利（只剩下一个棋子）。
     * 点击对话框上的按钮，可以重新开始游戏。
     * 在扩展版本中，你需要在这里添加一个输入框，让用户输入他的名字。
     */
    private void showVictoryDialog() {
        SharedPreferences sp = getSharedPreferences("player_info",MODE_PRIVATE);
        SharedPreferences.Editor sp_editor = sp.edit();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("胜利");
        dialogBuilder.setMessage("你赢了！");
        dialogBuilder.setMessage("请输入你的名字：");
        final EditText text = new EditText(this);
        dialogBuilder.setView(text);
        dialogBuilder.setPositiveButton("再来一局", (dialogInterface, i) -> {
            name = text.getText().toString();
            if(sp.getInt(map_name,2147483647) > _numberOfSteps){
                sp_editor.putInt(map_name,_numberOfPegs);
            }
            sp_editor.apply();
            chooseBoard();  // 重新开始游戏
        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    /**
     * 显示一个对话框，表明游戏已经失败（没有可移动的棋子）。
     * 点击对话框上的按钮，可以重新开始游戏。
     */
    private void showFailureDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("失败");
        dialogBuilder.setMessage("你输了！");
        dialogBuilder.setPositiveButton("再来一局", (dialogInterface, i) -> {
            chooseBoard();  // 重新开始游戏
        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    /**
     * 给定一个起始位置和目标位置。
     * 如果移动合法，返回被跳过的位置。
     * 如果移动不合法，返回 {@code null}。
     * 移动合法的定义，参见作业文档。
     *
     * @param startPos  起始位置
     * @param targetPos 目标位置
     * @return 移动合法时，返回一个新{@code SpacePosition}
     * 表示被跳过的位置；否则返回 {@code null}
     */
    private SpacePosition getSkippedPosition(SpacePosition startPos, SpacePosition targetPos) {
        SpacePosition skip = null;
        int staCol = startPos.getIndexColumn();
        int staRow = startPos.getIndexRow();
        int tarCol = targetPos.getIndexColumn();
        int tarRow = targetPos.getIndexRow();
        if(staCol == tarCol){
            if(staRow-tarRow == 2){
                skip = new SpacePosition(staCol,staRow-1);
            } else if (tarRow-staRow == 2) {
                skip = new SpacePosition(staCol,staRow+1);
            }
        } else if (staRow ==tarRow) {
            if(staCol-tarCol == 2){
                skip = new SpacePosition(staCol-1,staRow);
            } else if (tarCol-staCol == 2) {
                skip = new SpacePosition(staCol+1,staRow);
            }
        }
        if(skip != null){
            int skipcol = skip.getIndexColumn();
            int skiprow = skip.getIndexRow();
            if(_placeArray[skipcol][skiprow] != PEG){
                skip = null;
            }
        }
        // TODO
        return skip;
    }

    /**
     * 返回是否还有可移动的位置。
     *
     * @return 如果还有可移动的位置，返回 {@code true}
     * 否则返回 {@code false}
     */
    private Boolean has_movable_places(){
        for(int i = 0; i < _sizeColumn; i++){
            for(int j = 0; j < _sizeRow; j++){
                if(_placeArray[i][j] == PEG){
                    if(i-2>=0){
                        if((_placeArray[i-2][j] == SPACE)&&(_placeArray[i-1][j] == PEG))
                            return true;
                    }
                    if(i+2<_sizeColumn){
                        if((_placeArray[i+2][j] == SPACE)&&(_placeArray[i+1][j] == PEG))
                            return true;
                    }
                    if(j-2>=0){
                        if((_placeArray[i][j-2] == SPACE)&&(_placeArray[i][j-1] == PEG))
                            return true;
                    }
                    if(j+2<_sizeRow){
                        if((_placeArray[i][j+2] == SPACE)&&(_placeArray[i][j+1] == PEG))
                            return true;
                    }
                    // TODO
                }
            }
        }
        return false;
    }
}
