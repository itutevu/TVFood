package com.example.user.tvfood.UI;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.tvfood.Common.LocaleUtils;
import com.example.user.tvfood.R;

import su.levenetc.android.textsurface.Text;
import su.levenetc.android.textsurface.TextBuilder;
import su.levenetc.android.textsurface.TextSurface;
import su.levenetc.android.textsurface.animations.Alpha;
import su.levenetc.android.textsurface.animations.AnimationsSet;
import su.levenetc.android.textsurface.animations.Circle;
import su.levenetc.android.textsurface.animations.Delay;
import su.levenetc.android.textsurface.animations.Parallel;
import su.levenetc.android.textsurface.animations.Rotate3D;
import su.levenetc.android.textsurface.animations.Sequential;
import su.levenetc.android.textsurface.animations.ShapeReveal;
import su.levenetc.android.textsurface.animations.SideCut;
import su.levenetc.android.textsurface.animations.Slide;
import su.levenetc.android.textsurface.animations.TransSurface;
import su.levenetc.android.textsurface.contants.Align;
import su.levenetc.android.textsurface.contants.Axis;
import su.levenetc.android.textsurface.contants.Direction;
import su.levenetc.android.textsurface.contants.Pivot;
import su.levenetc.android.textsurface.contants.Side;
import su.levenetc.android.textsurface.contants.TYPE;

public class Activity_About extends AppCompatActivity {
    private TextSurface txt_TextAbout;


    public Activity_About() {
        LocaleUtils.updateConfig(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__about);
        txt_TextAbout = (TextSurface) findViewById(R.id.txt_TextAbout);

        txt_TextAbout.postDelayed(new Runnable() {
            @Override
            public void run() {
                show();
            }
        }, 1000);
    }

    private void show() {
        txt_TextAbout.reset();
        play(txt_TextAbout);
    }


    public void play(TextSurface textSurface) {

        //final Typeface robotoBlack = Typeface.createFromAsset(Activity_About.this.getAssets(), "fonts/RobotoBlack.ttf");
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //paint.setTypeface(robotoBlack);

       /* Text text1 = TextBuilder
                .create("TVFood")
                .setPaint(paint)
                .setSize(64)
                .setAlpha(0)
                .setColor(Color.WHITE)
                .setPosition(Align.SURFACE_CENTER).build();*/

        Text text2 = TextBuilder
                .create("TVFood")
                .setPaint(paint)
                .setSize(64)
                .setAlpha(0)
                .setColor(Color.WHITE)
                .setPosition(Align.SURFACE_CENTER).build();

        Text text3 = TextBuilder
                .create("Trải nghiệm ứng dụng")
                .setPaint(paint)
                .setSize(32)
                .setAlpha(0)
                .setPadding(10, 0, 10, 0)
                .setColor(getResources().getColor(R.color.colorBottomBar))
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, text2).build();


        Text text4 = TextBuilder
                .create("MIỄN PHÍ")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setPadding(0, 20, 0, 0)
                .setColor(getResources().getColor(R.color.colorBottomBar))
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, text3).build();

        Text text5 = TextBuilder
                .create("Với những")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(getResources().getColor(R.color.colorBottomBar))
                .setPosition(Align.SURFACE_CENTER).build();

        Text text6 = TextBuilder
                .create("Tính năng nổi bật:")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(Color.WHITE)
                .setPosition(Align.RIGHT_OF, text5).build();

        Text text7 = TextBuilder
                .create("Tìm địa điểm")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(getResources().getColor(R.color.colorBottomBar))
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, text6).build();

        Text text8 = TextBuilder
                .create("Chỉ dẫn đường")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(getResources().getColor(R.color.colorBottomBar))
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, text7).build();

        Text text9 = TextBuilder
                .create("Đánh giá, chia sẻ")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(getResources().getColor(R.color.colorBottomBar))
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, text8).build();

        Text text10 = TextBuilder
                .create("Và nhiều tính năng khác")
                .setPaint(paint)
                .setSize(32)
                .setAlpha(0)
                .setColor(getResources().getColor(R.color.colorBottomBar))
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, text9).build();

        Text textA = TextBuilder.create("Tính năng nổi bật:").setColor(getResources().getColor(R.color.colorBottomBar)).setPosition(Align.RIGHT_OF, text5).build();
        Text textB = TextBuilder.create("Tìm địa điểm").setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textA).build();
        Text textC = TextBuilder.create("Chỉ dẫn đường").setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textB).build();
        Text textD = TextBuilder.create("Đánh giá, chia sẻ").setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textC).build();
        Text textE = TextBuilder.create("Và nhiều tính năng khác").setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textD).build();

        final int flash = 1500;


        textSurface.play(
                new Sequential(
                        ShapeReveal.create(text2, 600, SideCut.show(Side.LEFT), false),

                        new Parallel(ShapeReveal.create(text2, 600, SideCut.hide(Side.LEFT), false),
                                new Sequential(Delay.duration(300), ShapeReveal.create(text2, 600, SideCut.show(Side.LEFT), false))),

                        new Parallel(new TransSurface(500, text3, Pivot.CENTER),
                                new Sequential(ShapeReveal.create(text3, 500, Circle.show(Side.CENTER, Direction.OUT), false))),


                        new Parallel(Delay.duration(300), new TransSurface(500, text4, Pivot.CENTER),
                                new Sequential(ShapeReveal.create(text4, 500, Circle.show(Side.CENTER, Direction.OUT), false))),

                        Delay.duration(500),
                        new Parallel(ShapeReveal.create(text2, 200, Circle.hide(Side.CENTER, Direction.OUT), true),
                                ShapeReveal.create(text3, 200, Circle.hide(Side.CENTER, Direction.OUT), true),
                                ShapeReveal.create(text4, 200, Circle.hide(Side.CENTER, Direction.OUT), true)),

                        Delay.duration(100),
                        new Sequential(new TransSurface(400, text5, Pivot.CENTER), Slide.showFrom(Side.LEFT, text5, 400)),
                        Delay.duration(400),




                        Delay.duration(500),
                        new Parallel(new TransSurface(700, textA, Pivot.CENTER),
                                Rotate3D.showFromCenter(textA, 700, Direction.CLOCK, Axis.X),
                                new AnimationsSet(TYPE.PARALLEL,
                                        ShapeReveal.create(textA, flash, SideCut.hide(Side.LEFT), false),
                                        new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(700), ShapeReveal.create(textA, flash, SideCut.show(Side.LEFT), false)),
                                        ShapeReveal.create(text5, 10, Circle.hide(Side.CENTER, Direction.OUT), true)
                                )),

                        Delay.duration(1000),
                        new AnimationsSet(TYPE.PARALLEL,
                                Rotate3D.showFromSide(textB, 500, Pivot.TOP),
                                new TransSurface(500, textB, Pivot.CENTER)
                        ),
                        Delay.duration(500),
                        new AnimationsSet(TYPE.PARALLEL,
                                Slide.showFrom(Side.TOP, textC, 500),
                                new TransSurface(1000, textC, Pivot.CENTER)
                        ),
                        Delay.duration(500),
                        new AnimationsSet(TYPE.PARALLEL,
                                ShapeReveal.create(textD, 500, Circle.show(Side.CENTER, Direction.OUT), false),
                                new TransSurface(1500, textD, Pivot.CENTER)
                        ),
                        Delay.duration(500),
                        new AnimationsSet(TYPE.PARALLEL,
                                ShapeReveal.create(textE, 500, Circle.show(Side.CENTER, Direction.OUT), false),
                                new TransSurface(1500, textE, Pivot.CENTER)
                        ),
                        Delay.duration(500),
                        new AnimationsSet(TYPE.PARALLEL,
                                new AnimationsSet(TYPE.PARALLEL, Alpha.hide(textE, 700), ShapeReveal.create(textE, 1000, SideCut.hide(Side.LEFT), true)),
                                new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(500), new AnimationsSet(TYPE.PARALLEL, Alpha.hide(textD, 700), ShapeReveal.create(textD, 1000, SideCut.hide(Side.LEFT), true))),
                                new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(1000), new AnimationsSet(TYPE.PARALLEL, Alpha.hide(textC, 700), ShapeReveal.create(textC, 1000, SideCut.hide(Side.LEFT), true))),
                                new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(1500), new AnimationsSet(TYPE.PARALLEL, Alpha.hide(textB, 700), ShapeReveal.create(textB, 1000, SideCut.hide(Side.LEFT), true))),
                                new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(2000), new AnimationsSet(TYPE.PARALLEL, Alpha.hide(textA, 700), ShapeReveal.create(textA, 1000, SideCut.hide(Side.LEFT), true))),
                                new TransSurface(6000, textA, Pivot.CENTER)
                        )

                )


                /* new Sequential(Delay.duration(200), ShapeReveal.create(text4, 500, Circle.show(Side.CENTER, Direction.OUT), false),
                                new ScaleSurface(500, text4, Fit.WIDTH)))*/

                       /* new Parallel(
                                new TransSurface(2000, text5, Pivot.CENTER),
                                new Sequential(
                                        new Sequential(ShapeReveal.create(text2, 500, Circle.show(Side.CENTER, Direction.OUT), false)),
                                        new Sequential(ShapeReveal.create(text3, 500, Circle.show(Side.CENTER, Direction.OUT), false)),
                                        new Sequential(ShapeReveal.create(text4, 500, Circle.show(Side.CENTER, Direction.OUT), false)),
                                        new Sequential(ShapeReveal.create(text5, 500, Circle.show(Side.CENTER, Direction.OUT), false))
                                )
                        )*/
                        /*Delay.duration(200),
                        new Parallel(
                                ShapeReveal.create(textThrowDamn, 1500, SideCut.hide(Side.LEFT), true),
                                new Sequential(Delay.duration(250), ShapeReveal.create(textDevilishGang, 1500, SideCut.hide(Side.LEFT), true)),
                                new Sequential(Delay.duration(500), ShapeReveal.create(textSignsInTheAir, 1500, SideCut.hide(Side.LEFT), true)),
                                Alpha.hide(texThyLamInnie, 1500),
                                Alpha.hide(textDaaiAnies, 1500)
                        )*/

        );

    }

}
