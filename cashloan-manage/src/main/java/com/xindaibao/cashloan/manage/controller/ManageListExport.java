package com.xindaibao.cashloan.manage.controller;

import com.xindaibao.cashloan.cl.service.*;
import com.xindaibao.cashloan.core.common.context.ExportConstant;
import com.xindaibao.cashloan.core.common.util.DateUtil;
import com.xindaibao.cashloan.core.common.util.JsonUtil;
import com.xindaibao.cashloan.core.common.util.excel.JsGridReportBase;
import com.xindaibao.cashloan.core.model.BorrowModel;
import com.xindaibao.cashloan.core.service.CloanUserService;
import com.xindaibao.cashloan.system.domain.SysDownloadLog;
import com.xindaibao.cashloan.system.domain.SysUser;
import com.xindaibao.cashloan.system.service.SysDownloadLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tool.util.StringUtil;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope("prototype")
@Controller
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ManageListExport extends ManageBaseController{

	@Resource
	private ClBorrowService clBorrowService;
	@Resource
	private CloanUserService userService;
	@Resource
	private OperatorReqLogService operatorReqLogService;
	@Resource
	private BorrowRepayLogService borrowRepayLogService;
	@Resource
	private PayLogService payLogService;
	@Resource
	private PayCheckService payCheckService;
	@Resource
	private UrgeRepayOrderService urgeRepayOrderService;
	@Autowired
	private SysDownloadLogService sysDownloadLogService;
	
	/**
	 * 导出还款记录报表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/modules/manage/borrowRepayLog/export.htm")
	public void repayLogExport(
			@RequestParam(value="searchParams",required = false) String searchParams) throws Exception {
		Map<String, Object> params = JsonUtil.parse(searchParams, Map.class);
		List list = borrowRepayLogService.listExport(params);
		SysUser user = (SysUser) request.getSession().getAttribute("SysUser");
		response.setContentType("application/msexcel;charset=UTF-8");
		// 记录取得
		String title = "还款记录Excel表";
		String[] hearders =  ExportConstant.EXPORT_REPAYLOG_LIST_HEARDERS;
		String[] fields = ExportConstant.EXPORT_REPAYLOG_LIST_FIELDS;
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportExcel(list,title,hearders,fields,user.getName());
		saveLog(list, user.getUserName(), SysDownloadLog.REPAY_LOG);
	}
	
	/**
	 * 导出借款订单报表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/modules/manage/borrow/export.htm")
	public void borrowExport(
			@RequestParam(value="searchParams",required = false) String searchParams) throws Exception {
		Map<String, Object> params = JsonUtil.parse(searchParams, Map.class);
		List list = clBorrowService.listBorrow(params);
		SysUser user = (SysUser) request.getSession().getAttribute("SysUser");
		response.setContentType("application/msexcel;charset=UTF-8");
		// 记录取得
		String title = "借款订单Excel表";
		String[] hearders =  ExportConstant.EXPORT_BORROW_LIST_HEARDERS;
		String[] fields = ExportConstant.EXPORT_BORROW_LIST_FIELDS;
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportExcel(list,title,hearders,fields,user.getName());
		saveLog(list, user.getUserName(), SysDownloadLog.LOAN_LOG);
	}
	
	/**
	 * 导出支付记录报表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/modules/manage/payLog/export.htm")
	public void payLogExport(
			@RequestParam(value="searchParams",required = false) String searchParams) throws Exception {
		List list = payLogService.listPayLog(searchParams);
		SysUser user = (SysUser) request.getSession().getAttribute("SysUser");
		response.setContentType("application/msexcel;charset=UTF-8");
		// 记录取得
		String title = "支付记录Excel表";
		String[] hearders =  ExportConstant.EXPORT_PAYLOG_LIST_HEARDERS;
		String[] fields = ExportConstant.EXPORT_PAYLOG_LIST_FIELDS;
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportExcel(list,title,hearders,fields,user.getName());
		saveLog(list, user.getUserName(), SysDownloadLog.PAY_LOG);
	}
	
	/**
	 * 导出支付对账记录报表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/modules/manage/payCheck/export.htm")
	public void payCheckExport(
			@RequestParam(value="searchParams",required = false) String searchParams) throws Exception {
		Map<String, Object> params = JsonUtil.parse(searchParams, Map.class);
		List list = payCheckService.listPayCheck(params);
		SysUser user = (SysUser) request.getSession().getAttribute("SysUser");
		response.setContentType("application/msexcel;charset=UTF-8");
		// 记录取得
		String title = "支付对账记录Excel表";
		String[] hearders =  ExportConstant.EXPORT_PAYCHECK_LIST_HEARDERS;
		String[] fields = ExportConstant.EXPORT_PAYCHECK_LIST_FIELDS;
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportExcel(list,title,hearders,fields,user.getName());
		saveLog(list, user.getUserName(), SysDownloadLog.PAY_CHECK_LOG);
	}
	
	/**
	 * 导出已逾期订单报表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/modules/manage/overdue/export.htm")
	public void overdueExport(
			@RequestParam(value="searchParams",required = false) String searchParams) throws Exception {
		Map<String, Object> params;
		if (StringUtil.isBlank(searchParams)) {
			params = new HashMap<>();
		}else {
			params = JsonUtil.parse(searchParams, Map.class);
		}
		params.put("state", BorrowModel.STATE_DELAY);
		List list = clBorrowService.listBorrow(params);
		SysUser user = (SysUser) request.getSession().getAttribute("SysUser");
		response.setContentType("application/msexcel;charset=UTF-8");
		// 记录取得
		String title = "已逾期订单Excel表";
		String[] hearders =  ExportConstant.EXPORT_OVERDUE_LIST_HEARDERS;
		String[] fields = ExportConstant.EXPORT_OVERDUE_LIST_FIELDS;
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportExcel(list,title,hearders,fields,user.getName());
		saveLog(list, user.getUserName(), SysDownloadLog.EXPORT_LOG);
	}
	
	/**
	 * 导出已坏账订单报表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/modules/manage/badDebt/export.htm")
	public void badDebtExport(
			@RequestParam(value="searchParams",required = false) String searchParams) throws Exception {
		Map<String, Object> params;
		if (StringUtil.isBlank(searchParams)) {
			params = new HashMap<>();
		}else {
			params = JsonUtil.parse(searchParams, Map.class);
		}
		params.put("state", BorrowModel.STATE_BAD);
		List list = clBorrowService.listBorrow(params);
		SysUser user = (SysUser) request.getSession().getAttribute("SysUser");
		response.setContentType("application/msexcel;charset=UTF-8");
		// 记录取得
		String title = "已坏账订单Excel表";
		String[] hearders =  ExportConstant.EXPORT_BADDEBT_LIST_HEARDERS;
		String[] fields = ExportConstant.EXPORT_BADDEBT_LIST_FIELDS;
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportExcel(list,title,hearders,fields,user.getName());
		saveLog(list, user.getUserName(), SysDownloadLog.BADDEBT_LOG);
	}
	
	/**
	 * 导出催收订单报表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/modules/manage/urgeRepayOrder/export.htm")
	public void urgeRepayOrderExport(
			@RequestParam(value="searchParams",required = false) String searchParams) throws Exception {
		Map<String, Object> params = JsonUtil.parse(searchParams, Map.class);
		List list = urgeRepayOrderService.listUrgeRepayOrder(params);
		SysUser user = (SysUser) request.getSession().getAttribute("SysUser");
		response.setContentType("application/msexcel;charset=UTF-8");
		// 记录取得
		String title = "催收订单Excel表";
		String[] hearders =  ExportConstant.EXPORT_REPAYORDER_LIST_HEARDERS;
		String[] fields = ExportConstant.EXPORT_REPAYORDER_LIST_FIELDS;
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportExcel(list,title,hearders,fields,user.getName());
		saveLog(list, user.getUserName(), SysDownloadLog.URGEREPAY_LOG);
	}
	
	/**
	 * 导出催收反馈报表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/modules/manage/urgeLog/export.htm")
	public void urgeLogExport(
			@RequestParam(value="searchParams",required = false) String searchParams) throws Exception {
		Map<String, Object> params = JsonUtil.parse(searchParams, Map.class);
		List list = urgeRepayOrderService.listUrgeLog(params);
		SysUser user = (SysUser) request.getSession().getAttribute("SysUser");
		response.setContentType("application/msexcel;charset=UTF-8");
		// 记录取得
		String title = "催收反馈Excel表";
		String[] hearders =  ExportConstant.EXPORT_URGELOG_LIST_HEARDERS;
		String[] fields = ExportConstant.EXPORT_URGELOG_LIST_FIELDS;
		JsGridReportBase report = new JsGridReportBase(request, response);
		report.exportExcel(list,title,hearders,fields,user.getName());
		saveLog(list, user.getUserName(), SysDownloadLog.URGE_FEEDBACK_LOG);
	}

	private void saveLog(List list, String userName, String menuName) {
		SysDownloadLog sysDownloadLog = new SysDownloadLog();
		sysDownloadLog.setCreate_time(DateUtil.getNow());
		if (list != null && !CollectionUtils.isEmpty(list)) {
			sysDownloadLog.setDownload_count(list.size());
		}
		sysDownloadLog.setUser_name(userName);
		sysDownloadLog.setDownload_menu(menuName);
		sysDownloadLogService.insert(sysDownloadLog);
	}

}