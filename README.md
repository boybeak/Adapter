# Adapter

This library can easily bind layout and ViewHolder.

## Installation [ ![Download](https://api.bintray.com/packages/boybeak/nulldreams/adapter/images/download.svg) ](https://bintray.com/boybeak/nulldreams/adapter/_latestVersion)

```groovy
implementation 'com.github.boybeak:adapter:x.y.z'
annotationProcessor 'com.github.boybeak:adapter-compiler:x.y.z'
```

This library must work with data binding. So, you need enable data binding in your project.

In your project's build.gradle, you need to enable dataBinding and add annotationProcessor.

```groovy
android {
    dataBinding {
        enabled = true
    }
}


dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    annotationProcessor "com.android.databinding:compiler:$gradle_version"
}
```

> Maybe you need change **annotationProcessor** into **kapt**. 
>
> ```groovy
> apply plugin: 'kotlin-kapt'
> ```
>
> Don't forget apply this plugin.

After this preparation, we start our own coding.

## Usage

A POJO class name [Crash.kt](https://github.com/boybeak/Adapter/blob/master/app/src/main/java/com/github/boybeak/autobind/Crash.kt)

```kotlin
class Crash() : Parcelable {

    var sdk_int: Int = 0
    var id: String? = null
    var incermental: String? = null
    var model: String? = null
    var simple_crash_info: String? = null
    var manufacturer: String? = null
    var code_name: String? = null
    var app_version: String? = null
    var role: Int? = null
    var admin_id: Int = 0
    var name: String? = null
    var crash_info: String? = null
    var crash_time: String? = null
    var studio_id: String? = null
    var studio_name: String? = null

    val causeBy: String
        get() = "Cause by:$simple_crash_info".trim()

    val manufacturerAndModel: String
        get() = "$manufacturer / $model"

    val nameWithRole: String
        get() = "$name · ${when(role){1->"T" 2->"S" 3->"F" else -> "Unknown"}}"

    fun formattedCrashInfo(): CharSequence {
        if (crash_info == null) {
            return ""
        }

        return HtmlCompat.fromHtml(
                crash_info!!.trim().replace("\n", "<p>").replace(":", ":<b>")
                        .replace(")", "</b>)"), HtmlCompat.FROM_HTML_MODE_COMPACT)
    }
}
```

```kotlin
adapter = DataBindingAdapter(this)
recycler_view.adapter = adapter
```

Bind Adapter and `RecyclerView` as usual. 

> Attenetion: the adapter is **DataBindingAdapter**. Or if you wanna custom your own adapter logic, just define custom class and extend [AbsAdapter](https://github.com/boybeak/Adapter/blob/master/adapter/src/main/java/com/github/boybeak/adapter/AbsAdapter.java)

Then define a ViewHolder class

```kotlin
@HolderInfo(layoutId = R.layout.layout_crash,
        layoutInfo = LayoutInfo(name = "CrashImpl", source = Crash::class))
class CrashHolder(b: LayoutCrashBinding) : AbsDataBindingHolder<CrashImpl, LayoutCrashBinding>(b) {
    override fun onBindData(context: Context, layout: CrashImpl, position: Int, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        binding().crash = layout.source
        binding().root.setOnClickListener {
            Intent(context, CrashInfoActivity::class.java)
                    .putExtra("crash", layout.source)
                    .startActivity(context)
        }
    }
}
```

The magic part is the annotation **@HolderInfo**. Add this annotion to a class and please don't extend AbsDataBindingHolder at this moment. Then build this project. After building complete, A java class named CrashImpl is generated at the same package of CrashHolder. And at this moment, you can make the CrashHolder extend AbsDataBindingHolder and override onBindData method.

> If you do not want an auto generated LayoutImpl java class. Leave the **@LayoutInfo** empty, And make a LayoutImpl class yourself.
>
> ```java
> public class CrashImpl extends AbsLayoutImpl<Crash> {
>   public CrashImpl(Crash source) {
>     super(source);
>   }
> 
>   @Override
>   public int getLayout() {
>     return R.layout.layout_crash;
>   }
> }
> ```
>
> Make sure of **getLayout** method returns the same value of **@HolderInfo.layotuId** field.

> If you do not want to use **@HolderInfo**, you can just define a CrashImpl class yourself .
>
> ```java
> public class CrashImpl extends AbsLayoutImpl<Crash> {
>   public CrashImpl(Crash source) {
>     super(source);
>   }
> 
>   @Override
>   public int getLayout() {
>     return R.layout.layout_crash;
>   }
>     
>   @Override
>   public Class<? extends AbsDataBindingHolder> getHolderClass() {
>     return CrashHolder.class;
>   }
> }
> ```
>
> Don't forget override **getHolderClass** method. If you choose this way, what you need know is this can lead to a slow performance because of runtime class reflection when creating holder.

Add Crash class to adapter.

```kotlin
adapter.addAll(crashList, object : Converter<Crash, CrashImpl> {
    override fun convert(data: Crash?, adapter: DataBindingAdapter): CrashImpl {
        return CrashImpl(data)
    }

}).autoNotify()
```



## @HolderInfo Details

**HolderInfo**

```java
public @interface HolderInfo {
    int layoutId();			//The layout you wanna bind with
    LayoutInfo layoutInfo();	//The LayoutImpl class info
}
```

**LayoutInfo**

```java
public @interface LayoutInfo {
    String name() default "";		//The LayoutImpl class name
    Class<?> source() default void.class;	//The sorce class, such as Crash.class
    Class<?>[] sourceGenerics() default {};	//The sorce's generic types, such as String for Crash<String> if Crash has generics

    Constructor[] constructors() default {}; //Constructors of LayoutImpl

    boolean supportSelect() default false;	// support select or not
    boolean selectable() default false;		// can be select or not, if you want set to true, you need set supportSelect to true
}
```

**Constructor**

```java
public @interface Constructor {
    boolean useId() default false; // true if you wan use super(source, id), then you can define yourself id for layoutimpl
    Member[] members() default {};	// Members of constructor method
}
```

**Member**

```java
public @interface Member {
    String name();	// the parameter name
    Class<?> type();	// the parameter type, such as List.class
    Class<?>[] generics() default {};	//the parameter's generics, such as {String.class}
}
```

