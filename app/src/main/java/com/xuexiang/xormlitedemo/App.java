/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.xormlitedemo;

import android.app.Application;
import android.content.Context;

import com.xuexiang.xaop.XAOP;
import com.xuexiang.xaop.util.PermissionUtils;
import com.xuexiang.xormlite.ExternalDataBaseRepository;
import com.xuexiang.xormlite.InternalDataBaseRepository;
import com.xuexiang.xormlite.annotation.DataBase;
import com.xuexiang.xormlite.enums.DataBaseType;
import com.xuexiang.xormlite.logs.DBLog;
import com.xuexiang.xormlitedemo.db.ExternalDataBase;
import com.xuexiang.xormlitedemo.db.InternalDataBase;
import com.xuexiang.xpage.AppPageConfig;
import com.xuexiang.xpage.PageConfig;
import com.xuexiang.xpage.PageConfiguration;
import com.xuexiang.xpage.model.PageInfo;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.List;

/**
 * <pre>
 *     desc   : 内部存储的数据库
 *     author : xuexiang
 *     time   : 2018/5/9 上午1:15
 * </pre>
 */
@DataBase(name = "internal", type = DataBaseType.INTERNAL)
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        init();

        InternalDataBaseRepository.getInstance()
                .setIDatabase(new InternalDataBase())  //设置内部存储的数据库实现接口
                .init(this);

        ExternalDataBaseRepository.getInstance()
                .setIDatabase(new ExternalDataBase(  //设置外部存储的数据库实现接口
                        ExternalDataBaseRepository.DATABASE_PATH,
                        ExternalDataBaseRepository.DATABASE_NAME,
                        ExternalDataBaseRepository.DATABASE_VERSION))
                .init(this);

        DBLog.debug(true);
    }

    /**
     * 初始化框架
     */
    private void init() {
        XUtil.init(this);
        XUtil.debug(true);

        PageConfig.getInstance().setPageConfiguration(new PageConfiguration() { //页面注册
            @Override
            public List<PageInfo> registerPages(Context context) {
                return AppPageConfig.getInstance().getPages(); //自动注册页面
            }
        }).debug("PageLog").enableWatcher(false).init(this);


        XAOP.init(this); //初始化插件
        XAOP.debug(true); //日志打印切片开启
        //设置动态申请权限切片 申请权限被拒绝的事件响应监听
        XAOP.setOnPermissionDeniedListener(new PermissionUtils.OnPermissionDeniedListener() {
            @Override
            public void onDenied(List<String> permissionsDenied) {
                ToastUtils.toast("权限申请被拒绝:" + StringUtils.listToString(permissionsDenied, ","));
            }

        });
    }
}
