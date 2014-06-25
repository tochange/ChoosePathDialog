package com.example.choosepathdialog;

import java.io.File;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChoosePathDialog extends Dialog
{

    public static final String m_ChooseAllModel = "*";

    private ChoosePathAdapter m_PathAdapter;

    private TextView m_CurrentPathTextView;

    private String m_ChooseFileFormat;

    /**
     * 
     * @Title WarningRulesImportDialog
     * @Description TODO
     * @param context
     * @param listener call back with a list fill with path which is checked
     * @param isChooseOneFile:true for choosing single file,false for multiple
     * @param chooseFileFormat:if choose file/files,pass file format,such as
     *            ".xml",or directory pass null instead,or event any kind of
     *            format pass "*",you can choose file and directory at the same
     *            time,if isChooseOneFile now is false.
     * @throws
     */
    public ChoosePathDialog(final Context context, boolean isChooseOneFile,
            String chooseFileFormat, final ChoosePathInterface listener)
    {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.callwarning_rulesimport_dialog);
        m_ChooseFileFormat = chooseFileFormat.toLowerCase();
        setCanceledOnTouchOutside(true);
        this.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);

        m_CurrentPathTextView = (TextView) findViewById(R.id.fileselectdialog_current_textView);
        Button m_SureBtn = (Button) findViewById(R.id.button1_freshpath);
        ListView m_PathLsit = (ListView) findViewById(R.id.fileselectdialog_listView);
        Button m_CancelBtn = (Button) findViewById(R.id.fileselectdialog_cancel_button);

        m_PathAdapter = new ChoosePathAdapter(context, m_CurrentPathTextView,
                isChooseOneFile, m_ChooseFileFormat);
        m_PathLsit.setOnItemClickListener(m_OnItemClickListener);
        m_PathLsit.setAdapter(m_PathAdapter);

        m_CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });

        m_SureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ArrayList<String> path = m_PathAdapter.getDataList();
                if (!path.isEmpty())
                {
                    listener.setPath(path);
                    dismiss();
                }
                else
                    Toast.makeText(context, "path is empty.", Toast.LENGTH_LONG)
                            .show();
            }
        });
    }

    private OnItemClickListener m_OnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> listview, View item,
                int position, long id)
        {
            String path = m_PathAdapter.getItem(position).toString().trim();
            File f = new File(path);
            if (f.isFile())
            {
                if (m_ChooseFileFormat != null
                        && (path.toLowerCase().endsWith(m_ChooseFileFormat) || (m_ChooseFileFormat
                                .equals(m_ChooseAllModel))))
                {
                    m_PathAdapter.updateRetList(path);
                    m_PathAdapter.notifyDataSetChanged();
                }

                return;
            }
            m_PathAdapter.initData(path);
            m_CurrentPathTextView.setText("current path："
                    + m_PathAdapter.getParentPath());
            m_PathAdapter.notifyDataSetChanged();
        }
    };
}
