package com.xxl.glue.admin.controller;

import com.xxl.glue.admin.core.model.Project;
import com.xxl.glue.admin.core.result.ReturnT;
import com.xxl.glue.admin.core.util.JacksonUtil;
import com.xxl.glue.admin.dao.IGlueInfoDao;
import com.xxl.glue.admin.dao.IProjectDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xuxueli on 17/5/29.
 */
@Controller
@RequestMapping("/project")
public class ProjectController {

    @Resource
    private IProjectDao projectDao;
    @Autowired
    private IGlueInfoDao glueInfoDao;


    @RequestMapping
    public String index(Model model){

        List<Project> projectList = projectDao.loadAll();
        model.addAttribute("projectList", JacksonUtil.writeValueAsString(projectList));

        return "project/project.list";
    }

    @RequestMapping("/save")
    @ResponseBody
    public ReturnT<String> save(Project project){

        // valid
        if (StringUtils.isBlank(project.getAppname())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入项目AppName");
        }
        if (!project.getAppname().matches("^[a-z0-9_]{4,20}$")) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "项目AppName正确格式为：长度4-20位的小写字母、数字和下划线");
        }
        if (StringUtils.isBlank(project.getName())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入项目名称");
        }

        // appname valid
        Project existProject = projectDao.findByAppname(project.getAppname());
        if (existProject != null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "项目AppName不可重复");
        }

        int ret = projectDao.save(project);
        return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
    }

    @RequestMapping("/update")
    @ResponseBody
    public ReturnT<String> update(Project project){

        // valid
        if (StringUtils.isBlank(project.getName())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入项目名称");
        }

        int ret = projectDao.update(project);
        return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ReturnT<String> delCode(int id){

        int count = glueInfoDao.pageListCount(0, 10, id, null);
        if (count > 0) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "拒绝删除，该项目下存在GLUE");
        }

        int ret = projectDao.delete(id);
        return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
    }

}
