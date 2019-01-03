package hewenle.com.mywidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.WindowManager;

/**
 * Created by hewenle on 2018/12/12 14:52
 * 虚线
 */
public class LineView extends AppCompatImageView {

    /**
     * 线的宽度
     */
    private int linewidth = 5;
    /**
     * 线的高度
     */
    private int linneHeight = 5;
    /**
     * 颜色
     */
    private String paintColor = "#1ABE25";
    /**
     * 两根线之间的距离
     */
    private int linespace = 5;

    /**
     *
     * @param linewidth 设置线的宽度
     */
    public void setLinewidth(int linewidth) {
        this.linewidth = linewidth;
    }

    /**
     *
     * @param linneHeight 设置线的高度
     */
    public void setLinneHeight(int linneHeight) {
        this.linneHeight = linneHeight;
    }

    /**
     *
     * @param paintColor 设置线的颜色
     */
    public void setPaintColor(String paintColor) {
        this.paintColor = paintColor;
    }

    /**
     *
     * @param linespace 设置两根线间隔
     */
    public void setLinespace(int linespace) {
        this.linespace = linespace;
    }

    private Paint paint;
    private float[] pts;

    public LineView(Context context) {
        this(context, null);
    }

    public LineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setColor(Color.parseColor(paintColor));
        paint.setStrokeWidth(linneHeight);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        pts = new float[width * 4 / (linewidth + linespace)];//保证能画完
        for (int i = 0; i < width * 4 / (linewidth + linespace); i++) {
            switch (i % 4) {
                case 0://起点x
                    if (i == 0) {
                        pts[i] = 0f;
                    } else {
                        pts[i] = pts[i - 2] + linespace;
                    }
                    break;
                case 1://起点y
                    pts[i] = 0f;
                    break;
                case 2://终点x
                    if (i == 2) {
                        pts[i] = linewidth;
                    } else {
                        pts[i] = pts[i - 2] + linewidth;

                    }


                    break;
                case 3://终点y
                    pts[i] = 0f;
                    break;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLines(pts, paint);
    }
}
