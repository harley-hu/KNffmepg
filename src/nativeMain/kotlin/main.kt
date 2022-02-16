import kotlinx.cinterop.*
import platform.android.JNIEnvVar
import platform.android.jobject
import platform.android.jstring
import ffmpeg.*

fun main() {
    println("Hello, Kotlin/Native!")
}


@CName("Java_com_personal_kotlinnative_MainActivity_stringFromJNI")
fun stringFromJNI(env: CPointer<JNIEnvVar>, thiz: jobject): jstring {
    memScoped {
        return env.pointed.pointed!!.NewStringUTF!!.invoke(env, "This is from Kotlin Native!!".cstr.ptr)!!
    }
}

@CName("Java_com_personal_kotlinnative_MainActivity_ffmpegInfo")
fun ffmpegInfo(env: CPointer<JNIEnvVar>, thiz: jobject): jstring {
    memScoped {
        val data = StringBuilder()
        var avcodec = av_codec_next(null)
        while (avcodec != null) {
            if (avcodec.pointed.decode != null) {
                data.append("decode:")
            } else {
                data.append("encode:")
            }
            when (avcodec.pointed.type) {
                AVMEDIA_TYPE_VIDEO -> data.append("(video)")
                AVMEDIA_TYPE_AUDIO -> data.append("(audio)")
                else -> {
                    data.append("(other)")
                }
            }
            data.append(avcodec.pointed.name?.toKString())
            data.append("\n")
            avcodec = avcodec.pointed.next
        }
        return env.pointed.pointed!!.NewStringUTF!!.invoke(env, data.toString().cstr.ptr)!!
    }
}