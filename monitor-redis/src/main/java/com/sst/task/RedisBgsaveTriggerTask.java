package com.sst.task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.sst.task.utils.LogUtils;
import com.sst.task.utils.TaskScheduler;

import redis.clients.jedis.Jedis;

/**
 * @author mixueqiang
 * @since May 30, 2017
 *
 */
@Service
public class RedisBgsaveTriggerTask implements Runnable {
  private static final Log LOG = LogFactory.getLog(RedisBgsaveTriggerTask.class);

  public RedisBgsaveTriggerTask() {
    TaskScheduler.register(getClass().getCanonicalName(), this, 15, 30 * 60);
  }

  @Override
  public void run() {
    try {
      InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("redis_nodes.properties");
      BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

      String line = null;
      while ((line = br.readLine()) != null) {
        if (StringUtils.startsWith(line, "#")) {
          continue;
        }

        Jedis jedis = null;
        try {
          String host = StringUtils.substringBefore(line, ":");
          String port = StringUtils.substringAfter(line, ":");

          jedis = new Jedis(host, Integer.parseInt(port));
          jedis.bgsave();
          LogUtils.info(LOG, "Bgsave is executing on {0}.", line);

          // 等待持久化结束。
          Thread.sleep(2 * 60 * 1000L);

        } catch (Throwable t) {
          LogUtils.error(LOG, t, "Failed to execute bgsave commond on {0}.", line);

        } finally {
          if (jedis != null) {
            jedis.close();
          }
        }

      }

    } catch (Throwable t) {
      LogUtils.error(LOG, t, "Failed to execute bgsave task.");
    }
  }

}
