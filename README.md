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
	     implementation 'com.github.flayone.OaidProject:myOaid:1.0.5'
	}
```

### 获取OAID

获取oaid结果有可能是异步返回的，使用时请注意这一点差异

```
 MyOAID.getOAID(this, new ResultCallBack() {
            @Override
            public void onResult(final String oaid) {
                //获取到的oaid结果，如果是未支持的设备，有可能返回值为空
            }
        });
```

### 版本更新

| 版本     | 日期         | 内容                                         | 
|--------|------------|--------------------------------------------|
| v1.0.5 | 2023.04.06 | 1.优化华为、荣耀手机获取方法 <br/>2.如果未获取到oaid值，现在会回调为空 |