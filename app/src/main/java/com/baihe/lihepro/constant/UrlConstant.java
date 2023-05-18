package com.baihe.lihepro.constant;

public class UrlConstant {

    public static String BASE_CRM_URL = "http://gcrmapi.hunli.baihe.com";

    /**
     * 登录
     */
    public static String LOGIN_URL = BASE_CRM_URL + "/outer/setting/login";
    public static String CANCEL_URL = BASE_CRM_URL+ "/outer/setting/cancel";

    public static String DEL_DEVICE_INFO = BASE_CRM_URL + "/outer/setting/delDeviceInfo";
    /**
     * 更新用户信息
     */
    public static String GET_USERINFOBY_ID_URL = BASE_CRM_URL + "/owner/setting/getuserinfobyid";
    /**
     * 发送验证码
     */
    public static String SEND_VERIFYCODE_URL = BASE_CRM_URL + "/outer/setting/sendVerifyCode";
    /**
     * 邀约列表
     */
    public static String INVITE_LIST_URL = BASE_CRM_URL + "/owner/customer/inviteList";
    /**
     * 客户详情-基本信息
     */
    public static String CUSTOMER_DETAIL_URL = BASE_CRM_URL + "/owner/customer/customerDetail";
    /**
     * 更新客户详情
     */
    public static String UPDATE_CUSTOMER_URL = BASE_CRM_URL + "/owner/customer/updateCustomer";
    /**
     * 客户详情-需求汇总
     */
    public static String CUSTOMER_LEADS_URL = BASE_CRM_URL + "/owner/leads/customerLeads";
    /**
     * 客户详情-邀约汇总
     */
    public static String REQ_LIST_URL = BASE_CRM_URL + "/owner/requirement/reqList";
    /**
     * 客户详情-销售动态
     */
    public static String FOLLOW_LIST_URL = BASE_CRM_URL + "/owner/common/followList";
    /**
     * 跟进详情
     */
    public static String FOLLOW_DETAIL_URL = BASE_CRM_URL + "/owner/common/followDetail";
    /**
     * 筛选配置
     */
    public static String FILTER_CONFIGT_URL = BASE_CRM_URL + "/owner/common/filterConfig";
    /**
     * 客户列表
     */
    public static String CUSTOMER_LIST_URL = BASE_CRM_URL + "/owner/customer/customerList";
    /**
     * 线索公海
     */
    public static String GET_CANDIDATE_INIT_URL = BASE_CRM_URL + "/owner/user/getCandidateInit";
    /**
     * 线索公海
     */
    public static String CREATE_FOLLOW_URL = BASE_CRM_URL + "/owner/common/createFollow";
    /**
     * 线索公海
     */
    public static String UPLOAD_URL = BASE_CRM_URL + "/owner/common/upload";
    /**
     * 新建客户基础信息
     */
    public static String CREATE_PARAMS_URL = BASE_CRM_URL + "/owner/customer/createParams";
    /**
     * 新建品类
     */
    public static String GET_CATEGORY_DATA_URL = BASE_CRM_URL + "/owner/customer/getCategoryData";
    /**
     * 新建客户
     */
    public static String CREATE_LEADS_URL = BASE_CRM_URL + "/owner/leads/create";
    /**
     * 联系人列表
     */
    public static String CONTACT_PARAMS_URL = BASE_CRM_URL + "/owner/customer/contactParams";
    /**
     * 新增联系人
     */
    public static String CREATE_CONTACT_URL = BASE_CRM_URL + "/owner/customer/createContact";
    /**
     * 新增品类-需求单
     */
    public static String CREATE_REQ_URL = BASE_CRM_URL + "/owner/requirement/createReq";
    /**
     * 新增品类-客户
     */
    public static String INSERT_LEADS_URL = BASE_CRM_URL + "/owner/leads/insertLeads";
    /**
     * 更新品类-需求单
     */
    public static String UPDATE_REQ_URL = BASE_CRM_URL + "/owner/requirement/updateReq";
    /**
     * 更新品类-客户
     */
    public static String UPDATE_LEADS_URL = BASE_CRM_URL + "/owner/leads/updateLeads";
    /**
     * 通用外呼
     */
    public static String ALL_DIAOUT_URL = BASE_CRM_URL + "/owner/callcenter/allDiaout";
    /**
     * 获取版本信息
     */
    public static String UPGRADE_URL = BASE_CRM_URL + "/outer/common/upgrade";
    /**
     * 销售列表
     */
    public static String ORDER_DATA_LIST_URL = BASE_CRM_URL + "/owner/order/orderDataList";
    /**
     * 订单领取
     */
    public static String CLAIM_ORDER_URL = BASE_CRM_URL + "/owner/order/claimOrder";
    /**
     * 首页列表
     */
    public static String APP_MENU_LIST_URL = BASE_CRM_URL + "/owner/menu/appMenuList";
    /**
     * 首页审核列表
     */
    public static String AUDIT_LIST_PAGE_URL = BASE_CRM_URL + "/owner/audit/auditListPage";
    /**
     * 审核列表
     */
    public static String AUDIT_LIST_URL = BASE_CRM_URL + "/owner/audit/auditList";
    /**
     * 订单详情
     */
    public static String ORDER_DETAIL_URL = BASE_CRM_URL + "/owner/order/orderDetail";
    /**
     * 合同列表
     */
    public static String CONTRACT_LIST_URL = BASE_CRM_URL + "/owner/contract/getContractListData";
    /**
     * 合同详情
     */
    public static String CONTRACT_DETAIL_URL = BASE_CRM_URL + "/owner/contract/contractDetail";
    /**
     * 审批详情
     */
    public static String AUDIT_DETAIL_URL = BASE_CRM_URL + "/owner/audit/auditInfo";
    /**
     * 构建器
     */
    public static String BUILD_PARAMS_URL = BASE_CRM_URL + "/owner/customer/buildParams";

    public static String BANQUET_USER_LIST = BASE_CRM_URL + "/owner/order/aKeyToBanquetUserList";
    public static String A_KEY_TO_BANQUET = BASE_CRM_URL + "/owner/order/aKeyToBanquet";



    public static String GET_ARGUMENT_INFO = BASE_CRM_URL + "/owner/contract/getAgreementInfo";
    /**
     * 获取品类
     */
    public static String COMPANY_CATEGORY_URL = BASE_CRM_URL + "/owner/common/companyCategory";
    /**
     * 产品列表
     */
    public static String PRODUCT_LIST_URL = BASE_CRM_URL + "/owner/product/getProductList";
    /**
     * 添加产品
     */
    public static String ADD_PRODUCT_URL = BASE_CRM_URL + "/owner/product/addProduct";
    /**
     * 新建合同
     */
    public static String CREATE_CONTRACT_URL = BASE_CRM_URL + "/owner/contract/createContract";
    /**
     * 编辑合同
     */
    public static String UPDATE_CONTRACT_URL = BASE_CRM_URL + "/owner/contract/updateContract";
    /**
     * 审批
     */
    public static String DO_AUDIT_URL = BASE_CRM_URL + "/owner/audit/doAudit";
    /**
     * 审批
     */
    public static String SUBMIT_AUDIT_URL = BASE_CRM_URL + "/owner/contract/submitAudit";
    /**
     * 协议新建和更新
     */
    public static String UPDATE_AGREEMENT_URL = BASE_CRM_URL + "/owner/contract/updateAgreement";
    public static String ADD_AGREEMENT_URL = BASE_CRM_URL + "/owner/contract/addAgreement";
    /**
     * 更新订单
     */
    public static String UPDATE_ORDER_URL = BASE_CRM_URL + "/owner/order/updateOrder";
    /**
     * 邀约首页tab项
     */
    public static String REQUIREMENT_PHASE_URL = BASE_CRM_URL + "/owner/common/getRequirementPhase";
    public static String CUSTOMER_SERVICE_PHASE_URL = BASE_CRM_URL + "/owner/common/getCustomerServicePhase";
    /**
     * 查看用户最重要信息
     */
    public static String CUSTOMER_IMPORTANT_INFO_URL = BASE_CRM_URL + "/owner/customer/customerImportantInfo";
    /**
     * 合同列表
     */
    public static String CONTRACT_LIST2_URL = BASE_CRM_URL + "/owner/contract/contractList";
    /**
     * 酒店列表
     */
    public static String HOTEL_NAME_LIST_URL = BASE_CRM_URL + "/owner/hotel/hotelNameList";
    /**
     * 内部提供人列表
     */
    public static String RECORD_USER_LIST_URL = BASE_CRM_URL + "/owner/xprules/getRecordUserList";

    public static String PAY_CODE_LIST_URL = BASE_CRM_URL + "/owner/paycode/payCodeList";

    public static String PAY_CODE_SEARCH_CUSTOMER = BASE_CRM_URL + "/owner/paycode/searchCustomer";
    public static String PAY_CODE_GET_PAY_QR_CODE = BASE_CRM_URL + "/owner/paycode/getPayQrCode";
    public static String PAY_CODE_CANCEL_QR_CODE = BASE_CRM_URL + "/owner/paycode/cancelQrcode";

    public static String SEARCH_USER = BASE_CRM_URL + "/owner/user/searchUser";
    /**
     * 获取客户的订单列表
     */
    public static String PAY_CODE_GET_ORDER_LIST = BASE_CRM_URL + "/owner/paycode/getOrderList";
    /**
     * 生成付款码
     */
    public static String PAY_CODE_CREATE_PAY_CODE = BASE_CRM_URL + "/owner/paycode/createPayCode";
    public static String OPREATE_TEAM = BASE_CRM_URL + "/owner/team/operateTeam";

    /**
     * 获取档期日历
     */
    public static String GET_USER_PERMISSION_LIST = BASE_CRM_URL + "/owner/common/getUserPermissionList";
    public static String CALENDAR_TIME_SLOT = BASE_CRM_URL + "/owner/calendar/timeSlot";
    public static String TRANS_CUSTOMER_REQ = BASE_CRM_URL + "/owner/customer/transferCustomerReq";
    public static String TRANS_CUSTOMER_LEADS = BASE_CRM_URL + "/owner/customer/transferCustomerLeads";
    public static String SCHEDULE_GET_CALENDAR = BASE_CRM_URL + "/owner/schedule/getCalendar";
    public static String SCHEDULE_GET_CALENDAR_NEW = BASE_CRM_URL + "/owner/schedule/getHomePageCalendar";
    public static String SCHEDULE_GET_HALL_INFO_BY_DATE = BASE_CRM_URL + "/owner/schedule/getHallInfoByDate";
    public static String SCHEDULE_GET_HALL_RESERVE_INFO = BASE_CRM_URL + "/owner/schedule/getHallReserveInfo";
    public static String SCHEDULE_GET_PAY_INFO = BASE_CRM_URL + "/owner/schedule/getPayInfo";
    public static String SCHEDULE_GET_CONTRACT_INFO = BASE_CRM_URL + "/owner/schedule/getContactInfo";
    public static String SCHEDULE_CHECK_ORDER = BASE_CRM_URL + "/owner/schedule/checkOrder";
    public static String SCHEDULE_CHECK_PAY = BASE_CRM_URL + "/owner/receivables/getPayStatus";
    public static String SCHEDULE_GET_COMPANY_LEVEL = BASE_CRM_URL + "/owner/schedule/getCompanyLevel";
    public static String SCHEDULE_COMMIT_RESERVE = BASE_CRM_URL + "/owner/calendar/commitReserve";
    public static String SCHEDULE_COMMIT_RESERVE_MULTI = BASE_CRM_URL + "/owner/calendar/commitReserveMulti";
    public static String SCHEDULE_COMMIT_BOOK = BASE_CRM_URL + "/owner/calendar/commitBook";
    public static String SCHEDULE_COMMIT_MULTI_BOOK = BASE_CRM_URL + "/owner/calendar/commitBookMulti";
    public static String SCHEDULE_LIST = BASE_CRM_URL + "/owner/schedule/list";
    public static String SCHEDULE_GET_RESERVE_INFO = BASE_CRM_URL + "/owner/calendar/getReserveInfo";
    public static String SCHEDULE_GET_BOOK_INFO = BASE_CRM_URL + "/owner/calendar/getBookInfo";
    public static String SCHEDULE_RESERVE_CANCEL_OR_DELAY = BASE_CRM_URL + "/owner/calendar/reserveCancelOrDely";
    public static String SCHEDULE_BOOK_CANCEL = BASE_CRM_URL + "/owner/calendar/bookCancel";
    public static String SCHEDULE_RESCHEDULE_HALL_RESERVE_INFO = BASE_CRM_URL + "/owner/schedule/rescheduleHallReserveInfo";
    public static String TEAM_GET_TEAM_INFO = BASE_CRM_URL + "/owner/team/getTeamInfo";
    public static String TEAM_GET_CATEGORY = BASE_CRM_URL + "/owner/team/getCategory";
    public static String GET_UNREAD_LIST = BASE_CRM_URL + "/owner/customer/unreadList";
    public static String UNREAD_STATUS = BASE_CRM_URL + "/owner/customer/unreadStatus";
    public static String RECEIVED_COUNT = BASE_CRM_URL + "/owner/customer/receivedCount";
    public static String CHANGE_RECEIVED_COUNT = BASE_CRM_URL + "/owner/customer/ChangeReceived";


    public static final String AGREEMENT_LIHE = "https://liheproapp.hunli.baihe.com" + "/static/agreement_lihe.html";

    /**
     * 案例库H5
     */
    public static final String ANLIKU_H5 = "https://anli.daoxila.com/#/home";

    public static final String ANLIKU_LOGIN = "https://anli.daoxila.com/#/login";

    /**
     * 隐私政策
     */

    public static final String PRIVACY_POLICY = "https://gcrmh5.hunli.baihe.com/tpl/yinsi.html";

}
