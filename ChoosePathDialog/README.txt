选择路径的对话框：这是一个对话框，返回用户选中的路径

第一个参数是程序上下文
第二个参数指定是否多选，若true能多选文件（文件夹），若false只能选中最新选择的
第三个参数指定格式，若选择某格式文件则传入相应类型，如要选择MP3文件，就传入“.MP3”，
               要xml文件传入".xml" 若要选择文件夹传入null，若文件夹和文件都要选择则传入"*"。
第四个参数是一个回调，获得所选路径集合。

如下：
    new ChoosePathDialog(this, false, "*", new ChoosePathInterface() {

            @Override
            public void setPath(ArrayList<String> pathList)
            {
                log.e("pathList size:" + pathList.size());
                log.e("pathList:" + pathList);
            }
        }).show();