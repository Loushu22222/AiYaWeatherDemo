package com.example.nanchen.weatherdemo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nanchen.weatherdemo.db.DbService;
import com.example.nanchen.weatherdemo.ui.PopMenu;
import com.example.nanchen.weatherdemo.url.CityUrl;
import com.example.nanchen.weatherdemo.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * Sqlist数据库
 */

public class CityManageActivity extends AppCompatActivity implements View.OnClickListener {

    private List<String> proList;
    private EditText editText_city_name;
    private Utils utils;
    private GridView gv;
    private List<String> list;
    private DbService dbService;
    private GvAdapter adapter;
    private String cityName;
    private TextView menu_tv_name;
    private ImageView menu_iv_back;
    private ImageButton menu_ib_add;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x111:
                    cityName = editText_city_name.getText().toString().trim();

                    if (list.contains(cityName)) {
                        Toast.makeText(CityManageActivity.this, "当前城市已添加！", Toast.LENGTH_SHORT).show();
                    } else {
                        dbService.insertCity(cityName);
                        Toast.makeText(CityManageActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        list.add(cityName);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 0x112:
                    Toast.makeText(CityManageActivity.this, "未检测到你输入的城市名!", Toast.LENGTH_SHORT).show();
                    break;
                case 0x113:
                    Toast.makeText(CityManageActivity.this, "网络不给力，稍后再试试吧！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_city_manage);

        initList();

        dbService = new DbService(this);
        list = new ArrayList<>();

        initView();

        list.addAll(dbService.findAll());

        adapter = new GvAdapter(this, R.layout.city_info_item, list);

        gv.setAdapter(adapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                final String cityName = list.get(position);
                //                Toast.makeText(CityManageActivity.this, cityName, Toast.LENGTH_SHORT).show();
                PopMenu popMenu = new PopMenu(CityManageActivity.this);
                popMenu.addItem("切换");
                popMenu.addItem("删除");
                popMenu.setOnPopMenuItemClickListener(new PopMenu.OnPopMenuItemClickListener() {
                    @Override
                    public void onItemClick(String title) {
                        switch (title) {
                            case "切换":
                                //                                Toast.makeText(CityManageActivity.this,"你点击了切换按钮",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CityManageActivity.this, MainActivity.class);
                                intent.putExtra("city", cityName);
                                startActivity(intent);
                                break;
                            case "删除":
                                dbService.delete(cityName);
                                Toast.makeText(CityManageActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                                list.remove(cityName);
                                adapter.notifyDataSetChanged();
                                break;
                        }
                    }
                });
                popMenu.showAsDropDown(view);
            }
        });
    }

    /**
     * 初始化省份
     */
    private void initList() {
        proList = new ArrayList<>();
        proList.add("内蒙古");
        proList.add("新疆");
        proList.add("西藏");
        proList.add("河北");
        proList.add("山西");
        proList.add("辽宁");
        proList.add("吉林");
        proList.add("黑龙江");
        proList.add("江苏");
        proList.add("浙江");
        proList.add("安徽");
        proList.add("福建");
        proList.add("江西");
        proList.add("山东");
        proList.add("河南");
        proList.add("湖北");
        proList.add("湖南");
        proList.add("广东");
        proList.add("广西");
        proList.add("海南");
        proList.add("四川");
        proList.add("贵州");
        proList.add("云南");
        proList.add("陕西");
        proList.add("甘肃");
        proList.add("青海");
        proList.add("宁夏");
        proList.add("台湾");

        proList.add("内蒙古省");
        proList.add("新疆省");
        proList.add("西藏省");
        proList.add("河北省");
        proList.add("山西省");
        proList.add("辽宁省");
        proList.add("吉林省");
        proList.add("黑龙江省");
        proList.add("江苏省");
        proList.add("浙江省");
        proList.add("安徽省");
        proList.add("福建省");
        proList.add("江西省");
        proList.add("山东省");
        proList.add("河南省");
        proList.add("湖北省");
        proList.add("湖南省");
        proList.add("广东省");
        proList.add("广西省");
        proList.add("海南省");
        proList.add("四川省");
        proList.add("贵州省");
        proList.add("云南省");
        proList.add("陕西省");
        proList.add("甘肃省");
        proList.add("青海省");
        proList.add("宁夏省");
        proList.add("台湾省");
    }


    private void initView() {
        menu_ib_add = (ImageButton) findViewById(R.id.menu_ib_add);
        menu_iv_back = (ImageView) findViewById(R.id.menu_image_back);
        menu_tv_name = (TextView) findViewById(R.id.menu_tv_name);
        menu_tv_name.setText("城市管理");
        gv = (GridView) findViewById(R.id.cma_gv);

        menu_iv_back.setOnClickListener(this);
        menu_ib_add.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_ib_add:
                showMyDialog();
                break;
            case R.id.menu_image_back:
                finish();
                break;
        }
    }

    /**
     * 显示自定义对话框
     */
    private void showMyDialog() {
        final Dialog dialog = new Dialog(this);
        View view = View.inflate(this, R.layout.dialog_layout, null);
        editText_city_name = (EditText) view.findViewById(R.id.dialog_et);
        Button btn_ok = (Button) view.findViewById(R.id.dialog_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cutCity();
                dialog.dismiss();
            }
        });
        Button btn_cancel = (Button) view.findViewById(R.id.dialog_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.setTitle("请输入需要添加的城市");
        dialog.show();
    }

    /**
     * 切换城市
     */
    private void cutCity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cityName = URLEncoder.encode(editText_city_name.getText().toString().trim(), "UTF-8");
                    if (proList.contains(editText_city_name.getText().toString().trim())) {//如果输入的是一个省份
                        handler.sendEmptyMessage(0x112);
                    } else {
                        String url = CityUrl.cityUrl + cityName;
                        utils = new Utils();
                        String json = utils.getData(url);
                        if (json != null) {
                            if (utils.parseJson(json)) {//说明此城市存在
                                handler.sendEmptyMessage(0x111);
                            } else {
                                handler.sendEmptyMessage(0x112);
                            }
                        } else {
                            handler.sendEmptyMessage(0x113);
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}

class GvAdapter extends ArrayAdapter<String> {

    public GvAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.city_info_item, null);
            vh = new ViewHolder();
            vh.tv_city_name = (TextView) convertView.findViewById(R.id.gv_item_city_name);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        String cityName = getItem(position);
        vh.tv_city_name.setText(cityName);
        return convertView;
    }

    class ViewHolder {
        TextView tv_city_name;
    }
}
