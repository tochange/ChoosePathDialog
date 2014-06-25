package com.example.choosepathdialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tochange.yang.lib.log;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class ChoosePathAdapter extends BaseAdapter
{
    private final String TAG = "ChoosePathAdapter";

    private List<String> m_Data;

    private ArrayList<String> mPathList;

    private String m_Parent;

    private Context m_Context;

    private String UPPER = "Upper Directory";

    public final String MNT = "/mnt";

    public final String EXTERNAL_SDCARD = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath();

    public final String USB_STORAGE = "/mnt/usb_storage";// no here

    private TextView pathView;

    private boolean isChooseOneFile;

    private String mChooseFileFormat;

    public ArrayList<String> getDataList()
    {
        return mPathList;
    }

    public String getParentPath()
    {
        return m_Parent;
    }

    public ChoosePathAdapter(Context context, TextView tv,
            boolean isChooseOneFile, String chooseFileFormat)
    {
        pathView = tv;
        mChooseFileFormat = chooseFileFormat;
        this.isChooseOneFile = isChooseOneFile;
        m_Data = new ArrayList<String>();
        mPathList = new ArrayList<String>();
        m_Parent = MNT;
        tv.setText("current path：" + m_Parent);
        m_Context = context;
        initData(m_Parent);
    }

    private boolean checkMonut(String path)
    {
        File f = new File(path);
        try
        {
            f.createNewFile();
        }
        catch (IOException e)
        {
            Log.e(TAG, f + " not exists");
            log.e(e.toString());
            return false;
        }
        if (f.exists())
        {
            f.deleteOnExit();
            return true;
        }
        else
            return false;
    }

    public void initData(String path)
    {
        if (m_Data != null && m_Data.size() > 0)
            m_Data.clear();
        if (path != null && path.equals(UPPER))
        {
            File f2 = new File(m_Parent);
            path = f2.getParent();
        }
        if (path != null && !path.equals(MNT))
        {
            m_Data.add(UPPER);
            File file = new File(path);
            if (file.exists())
            {
                File[] files = file.listFiles();
                if (files != null && files.length > 0)
                {
                    Arrays.sort(files);
                    for (File f : files)
                    {
                        log.d(f.getPath());
                        if (mChooseFileFormat != null)
                        {// 1.directory 2.not all but the format 3.all any
                         // format
                            if (f.isDirectory()
                                    || ((!mChooseFileFormat
                                            .equals(ChoosePathDialog.m_ChooseAllModel)) && f
                                            .getAbsolutePath().toLowerCase()
                                            .endsWith(mChooseFileFormat))
                                    || mChooseFileFormat
                                            .equals(ChoosePathDialog.m_ChooseAllModel))
                            {
                                m_Data.add(f.getPath());
                            }
                        }
                        else
                        {// directory only
                            if (f.isDirectory())
                                m_Data.add(f.getPath());
                        }
                    }
                }
            }
            else
            {
                Toast.makeText(m_Context, "null path", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (path != null && path.equals(MNT))
        {
            if (new File(EXTERNAL_SDCARD).exists())
                m_Data.add(EXTERNAL_SDCARD);
            else
                pathView.setText("are you sure " + EXTERNAL_SDCARD
                        + " plug in device?");

            // no need to check write permission
            // if (checkMonut(EXTERNAL_SDCARD))
            // m_Data.add(EXTERNAL_SDCARD);
            // if (checkMonut(USB_STORAGE))
            // m_Data.add(USB_STORAGE);
        }
        m_Parent = path;
    }

    private final class ViewHolder
    {
        private TextView textview;

        private CheckBox radiobutton;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder vh = null;
        Listener listener = null;
        if (null == convertView)
        {
            vh = new ViewHolder();
            convertView = View.inflate(m_Context,
                    R.layout.callwarning_rulesimport_listitem, null);
            vh.textview = (TextView) convertView
                    .findViewById(R.id.fileselectdialog_listview_path_textView);
            vh.radiobutton = (CheckBox) convertView
                    .findViewById(R.id.fileselectldialog_listview_radioButton);
            listener = new Listener();
            vh.radiobutton.setOnClickListener(listener);
            convertView.setTag(vh.radiobutton.getId(), listener);
            convertView.setTag(vh);
        }
        else
        {
            vh = (ViewHolder) convertView.getTag();
            listener = (Listener) convertView.getTag(vh.radiobutton.getId());
        }
        final String item = m_Data.get(position);
        String path = "";
        if (item != null && item.contains(File.separator))
        {
            int index = item.lastIndexOf(File.separator);
            path = item.substring(index + 1);
        }
        else
            path = item;// upper
        vh.textview.setTextColor(new File(item).isFile() ? Color.BLACK
                : Color.GREEN);
        vh.textview.setText(path);

        // 1.null means directory 2.all
        if (((mChooseFileFormat != null) && (!mChooseFileFormat
                .equals(ChoosePathDialog.m_ChooseAllModel)))
                && !(item.toLowerCase().endsWith(mChooseFileFormat))
                || item.equals(UPPER))
            vh.radiobutton.setVisibility(View.INVISIBLE);
        else
        {
            listener.setItem(item);
            vh.radiobutton.setVisibility(View.VISIBLE);
            vh.radiobutton.setChecked(mPathList.contains(item));
        }
        return convertView;
    }

    private final class Listener implements View.OnClickListener
    {
        String item;

        void setItem(String item)
        {
            this.item = item;
        }

        @Override
        public void onClick(View v)
        {
            updateRetList(item);
            notifyDataSetChanged();
        }

    }

    public boolean updateRetList(String path)
    {
        boolean has = false;
        if (isChooseOneFile)
        {
            if (mPathList.contains(path))
                has = true;
            mPathList.clear();
            if (has)
            {
                Log.e(TAG,
                        "mPathList:" + mPathList.size() + ":"
                                + Arrays.toString(mPathList.toArray()));
                return true;
            }
        }
        if (!mPathList.contains(path))
            mPathList.add(path);
        else
            mPathList.remove(path);

        Log.e(TAG,
                "mPathList:" + mPathList.size() + ":"
                        + Arrays.toString(mPathList.toArray()));
        return true;
    }

    @Override
    public int getCount()
    {
        return m_Data.size();
    }

    @Override
    public Object getItem(int position)
    {
        return m_Data.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }
}
