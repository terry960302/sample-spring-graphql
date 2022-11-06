package com.ritier.samplespringgraphql.graphql.dataloader

import com.netflix.graphql.dgs.DgsDataLoader
import com.ritier.samplespringgraphql.entity.AccountRole
import com.ritier.samplespringgraphql.service.UserService
import org.dataloader.MappedBatchLoader
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage


@DgsDataLoader(name = "roles")
class AccountRoleDataLoader : MappedBatchLoader<Long, List<AccountRole>>{
    @Autowired
    private lateinit var userService: UserService
    override fun load(keys: MutableSet<Long>?): CompletionStage<MutableMap<Long, List<AccountRole>>> {
        return CompletableFuture.supplyAsync { userService.getAccountRolesByCredentialIds(keys!!.stream().toList()) }
    }

}