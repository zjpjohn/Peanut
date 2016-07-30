package com.dhn.peanut.create;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dhn.peanut.MainActivity;
import com.dhn.peanut.R;
import com.dhn.peanut.util.AuthoUtil;
import com.dhn.peanut.util.PeanutInfo;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateActivity extends AppCompatActivity {

    private static final int CHOOSE_PICTURE = 0;


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_text_name)
    EditText mEtName;
    @BindView(R.id.edit_text_description)
    EditText mEtDescrition;
    @BindView(R.id.edit_text_tags)
    EditText mEtTag;
    @BindView(R.id.image_view)
    ImageView mIvHolder;

    private Bitmap mBitmap;
    private String mFilePath;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        ButterKnife.bind(this);

        initView();

    }

    private void initView() {

        initToolbar();

        mIvHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictrue();
            }
        });
    }

    private void takePictrue() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 3);
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, CHOOSE_PICTURE);
    }

    private void initToolbar() {
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setTitle("创建作品");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.arrow_left);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CHOOSE_PICTURE && data != null) {
                //保存返回的bitmap
                mBitmap = data.getParcelableExtra("data");
                mIvHolder.setImageBitmap(mBitmap);
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            if (mDialog != null) {
                mDialog.setCanceledOnTouchOutside(true);
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                    return false;
                }
            }

            String title = mEtName.getText().toString();
            String desc = mEtDescrition.getText().toString();
            String tag = mEtTag.getText().toString();

            if (!TextUtils.isEmpty(title) ||
                    !TextUtils.isEmpty(desc) ||
                    !TextUtils.isEmpty(tag) ||
                    mBitmap != null) {
                dialog();
            } else {
                finish();
            }
        }
        return false;

    }

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定丢弃该内容？");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                CreateActivity.this.finish();
                return;
            }
        });
        builder.setNegativeButton("取消", new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                return;
            }
        });

        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_send) {
            //上传图片
            upLoadImage(mBitmap);
        } else if (item.getItemId() == android.R.id.home) {
            String title = mEtName.getText().toString();
            String desc = mEtDescrition.getText().toString();
            String tag = mEtTag.getText().toString();

            if (!TextUtils.isEmpty(title) ||
                    !TextUtils.isEmpty(desc) ||
                    !TextUtils.isEmpty(tag) ||
                    mBitmap != null) {
                dialog();
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 上传图片
     */
    private void upLoadImage(Bitmap bitmap) {
        //显示对话框
        View layout = getLayoutInflater().inflate(R.layout.dialog_send, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(layout);
        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(false);


        String name = mEtName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "标题必须填写", Toast.LENGTH_SHORT).show();
            return;
        }
        if (bitmap == null) {
            Toast.makeText(this, "请先选择图片", Toast.LENGTH_SHORT).show();
            return;
        }

        mDialog.show();
        //将bitmap转化为文件，保存文件路径
        String fileName = "tem" + new Random().nextInt() + ".png";
        File file = bitmapToFile(bitmap, fileName);
        mFilePath = file.getAbsolutePath();

        //开启子线程上传,第一个参数为url， 第二个参数为图片路径
        String titile = mEtName.getText().toString();
        new UpLoadAsyncTask(PeanutInfo.URL_CREATE, mFilePath, titile).execute();
    }

    private File bitmapToFile(Bitmap bitmap, String fileName) {
        File f = new File(getCacheDir(), fileName);
        try {
            f.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            //将bitmap转化为字节数组
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            //将字节数组转化为文件输出流
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }


    class UpLoadAsyncTask extends AsyncTask<Void, Void, Integer> {

        private final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        private static final String PREFIX = "--";
        private static final String LINE_END = "\r\n";
        private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        private static final String TAG = "UpLoadAsyncTAsk";

        private String postUrl;
        private String filePath;
        private String title;

        UpLoadAsyncTask(String url, String path, String title) {
            postUrl = url;
            filePath = path;
            this.title = title;
        }

        @Override
        protected Integer doInBackground(Void... params) {

            //参数
            Map<String, String> parm = new HashMap<>();
            parm.put("Authorization", "Bearer " + AuthoUtil.getToken());
            parm.put("title", title);
            Integer retStr = uploadFile(filePath, postUrl, parm);
            return retStr;
        }

        @Override
        protected void onPostExecute(Integer respnoseCode) {
            super.onPostExecute(respnoseCode);

            //TODO
            Log.e(TAG, "response code:" + respnoseCode);
            if (respnoseCode == 200 || respnoseCode == 202) {
                Toast.makeText(CreateActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
                finish();
            } else if (respnoseCode == 401){
                Toast.makeText(CreateActivity.this, "只有Player才能发布作品", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CreateActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        }


        private Integer uploadFile(String file, String postUrl, Map<String, String> param) {
            String retStr = null;
            int responseCode = 0;
            try {
                URL url = new URL(postUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10 * 1000);
                conn.setConnectTimeout(10 * 1000);
                conn.setDoInput(true); // 允许输入流
                conn.setDoOutput(true); // 允许输出流
                conn.setUseCaches(false); // 不允许使用缓存
                conn.setRequestMethod("POST"); // 请求方式
                conn.setRequestProperty("Charset", "utf-8"); // 设置编码
                conn.setRequestProperty("connection", "keep-alive");
                conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
                conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb = null;
                String params = "";

                //添加参数
                if (param != null && param.size() > 0) {
                    Iterator<String> it = param.keySet().iterator();
                    while (it.hasNext()) {
                        sb = new StringBuffer();
                        String key = it.next();
                        String value = param.get(key);
                        sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                        sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
                        sb.append(value).append(LINE_END);
                        params = sb.toString();
                        Log.i("TAG", key + "=" + params + "##");
                        dos.write(params.getBytes());
                        dos.flush();
                    }
                }

                //TODO 添加类型
                sb = new StringBuffer();
                sb.append("Content-Type:image/png" + LINE_END); // 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
                sb.append(LINE_END);
                params = sb.toString();
                dos.write(params.getBytes());

                /**上传文件*/
                int len = 0;
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();

                //获取响应码
                responseCode = conn.getResponseCode();

                //TODO
                InputStream input = conn.getInputStream();
                StringBuffer sb1 = new StringBuffer();
                int ss;
                while ((ss = input.read()) != -1) {
                    sb1.append((char) ss);
                }
                retStr = sb1.toString();
                Log.e(TAG, "result : " + retStr);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseCode;
        }
    }
}



