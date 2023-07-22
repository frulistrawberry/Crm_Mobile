package com.baihe.lib_contract.service

import android.app.Activity
import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.baihe.lib_common.constant.RoutePath
import com.baihe.lib_common.service.IContractService
import com.baihe.lib_contract.ui.activity.AddOrUpdateContractActivity
import com.baihe.lib_contract.ui.activity.ContractDetailActivity
import com.baihe.lib_contract.ui.activity.ContractListActivity

@Route(path = RoutePath.CONTRACT_SERVICE_CONTRACT)
class ContractServiceImp:IContractService {
    override fun toContractList(context: Context) {
        ContractListActivity.start(context)
    }

    override fun toContractDetail(context: Context, id: String) {
        ContractDetailActivity.start(context,id)
    }

    override fun toAddOrUpdateContract(act: Activity, orderId: String, contractId: String?) {
        AddOrUpdateContractActivity.start(act,orderId,contractId)
    }
}