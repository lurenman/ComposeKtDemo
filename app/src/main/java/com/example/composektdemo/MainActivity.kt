package com.example.composektdemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composektdemo.ui.theme.ComposeKtDemoTheme
import com.example.composektdemo.ui.theme.Purple700
import kotlinx.coroutines.launch

/**
 * 参考
 * https://developer.android.google.cn/jetpack/compose/tutorial?hl=zh-cn
 * https://blog.csdn.net/m0_71524094/article/details/125725511
 * https://blog.csdn.net/pfourfire/article/details/123090680
 * //material相关的
 * https://developer.android.google.cn/reference/kotlin/androidx/compose/material/package-summary?authuser=0&hl=tr#components
 */

class MainActivity : ComponentActivity() {
    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContent {
            ComposeKtDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val scaffoldState =
                        rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
                    val coroutineScope = rememberCoroutineScope()
                    Scaffold(
                        scaffoldState = scaffoldState,
                        topBar = {
                            TopBar(
                                onMenuClicked = {
                                    coroutineScope.launch {
                                        scaffoldState.drawerState.open()
                                        Log.d(
                                            TAG,
                                            "topBar onMenuClicked thread:${Thread.currentThread().name}"
                                        )
                                    }
                                })
                        },
                        drawerContent = {
                            Drawer()
                        },
                        bottomBar = { BottomBar() }, floatingActionButton = {
                            FloatingActionButton(
                                onClick = {
                                    coroutineScope.launch {
                                        when (scaffoldState.snackbarHostState.showSnackbar(
                                            message = "Snack Bar",
                                            actionLabel = "Dismiss"
                                        )) {
                                            SnackbarResult.Dismissed -> {
                                            }

                                            SnackbarResult.ActionPerformed -> {
                                            }
                                        }
                                    }
                                }
                            ) {
                                Text(text = "+", color = Color.White, fontSize = 26.sp)
                            }
                        }
                    ) {
                        Content()
                    }
                }
            }
        }
    }

    @Composable
    fun TopBar(onMenuClicked: () -> Unit) {
        TopAppBar(
            title = {
                Text(text = "ComposeKtDemo", color = Color.White)
            },
            navigationIcon = {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    modifier = Modifier.clickable(onClick = onMenuClicked),
                    tint = Color.White
                )
            },
            backgroundColor = Purple700,
            elevation = 12.dp
        )
    }

    @Composable
    fun Drawer() {
        Column(
            Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            repeat(5) { item ->
                Text(text = "Item $item", modifier = Modifier.padding(8.dp), color = Color.Black)
            }
        }
    }

    @Composable
    fun BottomBar() {
        BottomAppBar(
            backgroundColor = Purple700
        ) {
            Text(text = "BottomAppBar", color = Color.White)
        }
    }

    @Composable
    fun Content() {
        Column(
            modifier = Modifier.padding(18.dp, 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Greeting("Android")
            //Spacer是空格布局,其背景色是透明的,Android原生的margin属性的替代组件(因为设计问题,compose组件只提供padding设置)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
            )
            ButtonEvent()
        }
    }

    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello Compose!")
        Text(text = "Hello $name!")
    }

    @Composable
    fun ButtonEvent() {
        Button(
            onClick = {
                Intent(mContext, ListActivity::class.java).run {
                    mContext.startActivity(this)
                }

            }, modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)

        ) {
            Text(text = "列表")
        }
        Button(
            onClick = { /*TODO*/ }, modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 0.dp, top = 30.dp, end = 0.dp, bottom = 0.dp)
        ) {
            Text(text = "test")
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        ComposeKtDemoTheme {
            Column() {
                Greeting("Android")
                ButtonEvent()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ButtonTest() {
        val context = LocalContext.current
        val interactionSource = remember {
            MutableInteractionSource()
        }
        //参考https://zhuanlan.zhihu.com/p/443729041
        //按压状态
        val pressState = interactionSource.collectIsPressedAsState()
        val borderColor = if (pressState.value) Color.Green else Color.Gray
        Column(modifier = Modifier.padding(10.dp, 10.dp)) {
            Button(
                onClick = {
                    Toast.makeText(context, "点击了登录", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .size(150.dp, 40.dp)
                    .clip(RoundedCornerShape(20.dp)),
                enabled = true,
                border = BorderStroke(1.dp, color = borderColor),
                interactionSource = interactionSource,
                shape = RoundedCornerShape(20),
                elevation = ButtonDefaults.elevation(2.dp, 8.dp, 0.dp)
            ) {
                Text(text = "buttonTest")
            }
        }
    }

}
