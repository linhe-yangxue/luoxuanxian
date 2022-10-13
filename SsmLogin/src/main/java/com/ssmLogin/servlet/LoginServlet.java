package com.ssmLogin.servlet;

import java.util.Map;

import com.ssmCore.constants.ReInfo;
import com.ssmCore.jetty.BaseServlet;
import com.ssmCore.jetty.FunUrl;
import com.ssmLogin.constant.I_Error_Login;
import com.ssmLogin.constant.I_ModuleServlet;
import com.ssmLogin.defdata.facde.I_Login;
import com.ssmLogin.defdata.impl.PlatformInfoImpl;
import com.ssmLogin.defdata.impl.UserLogin;
import com.ssmShare.constants.E_PlateInfo;
import com.ssmShare.platform.DataConf;

public class LoginServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    private DataConf dSource = DataConf.getInstance();

    public LoginServlet() {
        super(LoginServlet.class, new HttpParamsPress());
    }

    /**
     * 游戏用户登录
     *
     * @param param
     * @return
     */
    @FunUrl(value = I_ModuleServlet.LONGIN_GAME)
    protected ReInfo login(Map<String, Object> param) {
        I_Login login = UserLogin.getInstance();
        try {
            dSource.load((String) param.get("gid"), (String) param.get("pid"),
                    E_PlateInfo.ALL.getType());
            if (dSource.doc == null) {
                PlatformInfoImpl.getInstance().initDB((String) param.get("gid"));
                dSource.load((String) param.get("gid"), (String) param.get("pid"), E_PlateInfo.ALL.getType());
            }
            if (dSource.doc != null) {
                return login.Login(param, dSource);
            }
            if (dSource.gid.equals("yqdq")) {
                return login.Login(param, dSource);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dSource.Destory();
            dSource = null;
            login = null;
        }
        return new ReInfo(I_Error_Login.ERRO_USER_GET, "用户平台未找到");
    }

    /**
     * 游戏用户登录
     *
     * @param param
     * @return
     */
    @FunUrl(value = I_ModuleServlet.GAME_LOGIN)
    protected ReInfo gamelogin(Map<String, Object> param) {
        I_Login login = UserLogin.getInstance();
        try {
            dSource.load((String) param.get("gid"), (String) param.get("pid"), E_PlateInfo.ALL.getType());

            if (dSource.doc == null) {
                PlatformInfoImpl.getInstance().initDB((String) param.get("gid"));
                dSource.load((String) param.get("gid"), (String) param.get("pid"), E_PlateInfo.ALL.getType());
            }
            if (dSource.doc != null) {
                return login.pLogin(param, dSource);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dSource.Destory();
            dSource = null;
            login = null;
        }
        return new ReInfo(I_Error_Login.ERRO_USER_GET, "用户平台未找到");
    }

    /**
     * 游戏用户登录
     *
     * @param param
     * @return
     */
    @FunUrl(value = I_ModuleServlet.TOKEN_LOGIN)
    protected ReInfo tokenLogin(Map<String, Object> param) {
        I_Login login = UserLogin.getInstance();
        try {
            dSource.load((String) param.get("gid"), (String) param.get("pid"),
                    E_PlateInfo.ALL.getType());

            if (dSource.doc == null) {
                PlatformInfoImpl.getInstance().initDB((String) param.get("gid"));
                dSource.load((String) param.get("gid"), (String) param.get("pid"), E_PlateInfo.ALL.getType());
            }
            if (dSource.doc != null) {
                return login.tokenUser(param, dSource);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dSource.Destory();
            dSource = null;
            login = null;
        }
        return new ReInfo(I_Error_Login.ERRO_USER_GET, "用户平台未找到");
    }
}
