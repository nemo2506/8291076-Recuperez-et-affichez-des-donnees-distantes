package com.jeremieguillot.appwritetest

import android.content.Context
import io.appwrite.Client

object Appwrite {
    private const val ENDPOINT = "https://cloud.appwrite.io/v1"
    private const val PROJECT_ID = "66c5a61900363b1dd0af"

    private lateinit var client: Client

    // Add this line 👇
    internal lateinit var ideas: IdeaService
    internal lateinit var account: AccountService

    fun init(context: Context) {
        client = Client(context)
            .setEndpoint(ENDPOINT)
            .setProject(PROJECT_ID)

        // Add this line 👇
        ideas = IdeaService(client)
        account = AccountService(client)
    }
}