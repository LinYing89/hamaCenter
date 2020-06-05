package com.bairock.iot.hamaCenter.controller;

import java.util.List;

import com.bairock.iot.hamaCenter.exception.MyException;
import com.bairock.iot.hamaCenter.utils.Result;
import com.bairock.iot.hamaCenter.utils.ResultEnum;
import com.bairock.iot.hamaCenter.utils.ResultUtil;
import com.bairock.iot.hamalib.data.DevGroupLoginResult;
import com.bairock.iot.hamalib.data.DragDevice;
import com.bairock.iot.hamalib.device.Device;
import com.bairock.iot.hamalib.device.devcollect.DevCollect;
import com.bairock.iot.hamalib.user.DevGroup;
import com.bairock.iot.hamalib.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bairock.iot.hamaCenter.communication.PadChannelBridgeHelper;
import com.bairock.iot.hamaCenter.service.DevGroupService;
import com.bairock.iot.hamaCenter.service.DragConfigService;
import com.bairock.iot.hamaCenter.service.DragDeviceService;

@RestController
@RequestMapping(value = "/group")
public class GroupController {

    @Autowired
    private DevGroupService devGroupService;
    @Autowired
    private DragDeviceService dragDeviceService;
    @Autowired
    private DragConfigService dragConfigService;

    // 打开注册页面
    @PostMapping("/add")
    public Result<?> addDevGroup(@PathVariable String userid, DevGroup group) {
        return ResultUtil.success(devGroupService.insert(group));
    }

    @PostMapping("/edit/{groupId}")
    public String editGroup(@PathVariable String groupId, DevGroup group) {
        devGroupService.editGroup(groupId, group);
        return "redirect:/loginSuccess";
    }

    @GetMapping("/del/{groupId}")
    public String deleteGroup(@PathVariable String groupId) {
        devGroupService.deleteGroup(groupId);
        return "redirect:/loginSuccess";
    }

    // 打开组页面
    @GetMapping("/{groupId}")
    public String showGroup(@PathVariable String groupId, Model model) {
        User user = (User) model.asMap().get("user");

        DevGroup group = user.findDevGroupById(groupId);
        List<Device> listDevState = group.findListIStateDev(true);
        List<DevCollect> listDevCollect = group.findListCollectDev(true);
        model.addAttribute("devGroup", group);
        model.addAttribute("listDevState", listDevState);
        model.addAttribute("listDevCollect", listDevCollect);
        return "group/group";
    }

    // 客户端组登录
    @ResponseBody
    @GetMapping("/client/devGroupLogin/{userName}/{groupName}/{groupPsd}")
    public Result<DevGroupLoginResult> devGroupLogin(@PathVariable String userName, @PathVariable String groupName,
                                                     @PathVariable String groupPsd) throws Exception {
        return devGroupService.devGroupLogin(userName, groupName, groupPsd);
    }

    // 客户端组登录
    @ResponseBody
    @GetMapping("/client/devGroupLogin/{loginModel}/{userName}/{groupName}/{groupPsd}")
    public Result<?> devGroupLogin2(@PathVariable String loginModel, @PathVariable String username,
            @PathVariable String groupName, @PathVariable String groupPsd) throws Exception {
        DevGroup devGroup = devGroupService.findByUsernameAndDevGroupNameAndDevGroupPassword(username, groupName, groupPsd);
        if(null == devGroup){
            throw new MyException(ResultEnum.NO_ENTITY);
        }

        if (loginModel.toLowerCase().equals("local")) {
            // 本地登录, 查看本地是否已有登录, 如果已有, 不允许本地登录
            boolean haved = PadChannelBridgeHelper.getIns().LocalLoginHaved(username, groupName);
            if (haved) {
                throw new MyException("账号已在其他设备上登录");
            }
        }
        return ResultUtil.success(devGroup);
    }

    // 客户端组上传
    @ResponseBody
    @PostMapping("/client/groupUpload")
    public Result<Object> userUpload(@RequestBody DevGroup devGroup) throws Exception {
        return devGroupService.groupUpload(devGroup);
    }

    // 客户端组内组态设备信息上传
    @ResponseBody
    @PostMapping("/client/dragDeviceUpload")
    public Result<Object> dragDeviceUpload(@RequestBody List<DragDevice> dragDevices) throws Exception {
        dragDeviceService.insert(dragDevices);
        Result<Object> result = new Result<>();
        return result;
    }

    // 客户端组内组态设备信息上传
    @ResponseBody
    @PostMapping("/client/dragConfigUpload")
    public Result<Object> dragConfigUpload(@RequestBody DragConfig dragConfig) throws Exception {
        dragConfigService.insert(dragConfig);
        Result<Object> result = new Result<>();
        return result;
    }

    // 客户端组下载
    @ResponseBody
    @GetMapping("/client/groupDownload/{userName}/{groupName}")
    public Result<DevGroup> userDownload(@PathVariable String userName, @PathVariable String groupName)
            throws Exception {
        return devGroupService.groupDownload(userName, groupName);
    }

    // 客户端组组态设备信息下载
    @ResponseBody
    @GetMapping("/client/dragDeviceDownload/{userName}/{groupName}")
    public Result<List<DragDevice>> dragDeviceDownload(@PathVariable String userName, @PathVariable String groupName)
            throws Exception {
        return devGroupService.dragDeviceDownload(userName, groupName);
    }

    // 客户端组组态配置信息下载
    @ResponseBody
    @GetMapping("/client/dragConfigDownload/{userName}/{groupName}")
    public Result<DragConfig> dragConfigDownload(@PathVariable String userName, @PathVariable String groupName) {
        return devGroupService.dragConfigDownload(userName, groupName);
    }
}
