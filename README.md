# OaidProject
获取Android手机oaid源代码，oaid值准确性和msa OAID SDK方式一致，代码透明，调用简洁，崩溃防护，更安全高效。

## 开始使用

### 引入SDK

在项目根目录的 `build.gradle` 中添加以下引入

```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

在APP目录的 `build.gradle` 中添加以下引入

```
	dependencies {
	        implementation 'com.github.flayone:OaidProject:v1.0.3'
	}
```


### 获取OAID

初始化

```
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化代码获取的oaid值
        MyOAID.init(this);
    }
}
```

获取oaid


```
MyOAID.getOAID(this)
```

