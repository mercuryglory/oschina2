package org.mercury.oschina.main.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.mercury.oschina.R;
import org.mercury.oschina.base.BaseActivity;
import org.mercury.oschina.emoji.EmojiView;
import org.mercury.oschina.utils.TDevice;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Mercury on 2016/8/15.
 * 弹一弹
 */
public class PublicTweetActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_send)
    TextView mTvSend;
    @BindView(R.id.search_bar)
    RelativeLayout mSearchBar;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.iv_img)
    ImageView mIvImg;
    @BindView(R.id.iv_clear_img)
    ImageView mIvClearImg;
    @BindView(R.id.rl_img)
    RelativeLayout mRlImg;
    @BindView(R.id.tv_clear)
    TextView mTvClear;
    @BindView(R.id.bottom)
    RelativeLayout mBottom;
    @BindView(R.id.ib_picture)
    ImageButton mIbPicture;
    @BindView(R.id.ib_mention)
    ImageButton mIbMention;
    @BindView(R.id.ib_trend_software)
    ImageButton mIbTrendSoftware;
    @BindView(R.id.ib_emoji_keyboard)
    ImageButton mIbEmojiKeyboard;
    @BindView(R.id.iv_show)
    ImageView mIvShow;
    @BindView(R.id.fl_emoji)
    FrameLayout flEmoji;

    private EditText mEt_album;
    private EditText mEt_viode;
    private Button mBt_dismiss;
    private AlertDialog mAlertDialog;
    private View mView1;

    private EmojiView mEmojiView;

    @Override
    protected int getContentView() {
        return R.layout.activity_tweet_pub;
    }

    @Override
    protected void initData() {
        super.initData();
        mEtContent.setFocusable(true);
        mEtContent.setFocusableInTouchMode(true);
        mEtContent.requestFocus();
    }



    private void init() {
        //接收从点击相册传过来的photo然后设置到本页面的imageview中
        Bitmap receive =(getIntent().getParcelableExtra("img_bitmap"));
        mIvShow.setImageBitmap(receive);

        mIvBack.setOnClickListener(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_send, R.id.search_bar, R.id.et_content, R.id.ib_picture, R.id.ib_mention, R.id.ib_trend_software, R.id.ib_emoji_keyboard})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_send:
                break;
            case R.id.search_bar:
                break;
            case R.id.et_content:
                break;
            case R.id.ib_picture:
                AlertDialog.Builder builder = new AlertDialog.Builder(PublicTweetActivity.this);
                mView1 = View.inflate(getApplicationContext(), R.layout.activity_text_dialog, null);
                builder.setView(mView1);
                mAlertDialog = builder.show();
                initDialog();
                break;
            case R.id.ib_mention:

                break;
            case R.id.ib_trend_software:
                String str = "#请输入软件名#";
                String text = mEtContent.getText().toString();
                mEtContent.setText(text + str);
                mEtContent.setSelection(text.length() + 1, text.length() + str.length() - 1);
                break;
            case R.id.ib_emoji_keyboard:
                if (mEmojiView == null) {
                    mEmojiView = new EmojiView(this, mEtContent);
                    flEmoji.addView(mEmojiView);
                }
                mEmojiView.openPanel();
                TDevice.closeKeyboard(mEtContent);

                break;
        }
    }

    private void initDialog() {
        mEt_album = (EditText) mView1.findViewById(R.id.et_album);
        mEt_viode = (EditText) mView1.findViewById(R.id.et_viode);
        mBt_dismiss = (Button) mView1.findViewById(R.id.bt_dismiss);
        mEt_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.PICK");
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });
        mEt_viode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
            }
        });
        mBt_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
    }



}
