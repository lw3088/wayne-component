FROM 127.0.0.1/library/jdk-time-as-skywalking:8-jdk-alpine
ENV LANG en_US.UTF-8
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories && apk add --update ttf-dejavu fontconfig && rm -rf /var/cache/apk/* && mkfontscale && mkfontdir && fc-cache
RUN mkdir -p /home/juser/project/wayne-component
COPY wayne-component-adapter/target/wayne-component.jar  /home/juser/project/wayne-component/wayne-component.jar
COPY starter.sh /home/juser/project/wayne-component/starter.sh
RUN chmod +x /home/juser/project/wayne-component/starter.sh && chown -R juser:juser /home/juser/project
USER juser
WORKDIR "/home/juser/project/wayne-component"
ARG projectEnv=test
ENV projectEnv ${projectEnv}
ENTRYPOINT ["/bin/sh", "-c", "/home/juser/project/wayne-component/starter.sh wayne-component.jar start ${projectEnv}"]