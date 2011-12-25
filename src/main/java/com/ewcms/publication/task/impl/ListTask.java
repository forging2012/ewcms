/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.publication.task.impl;
 
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.xwork.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.core.site.model.Channel;
import com.ewcms.core.site.model.Site;
import com.ewcms.publication.freemarker.generator.ListGenerator;
import com.ewcms.publication.generator.Generatorable;
import com.ewcms.publication.service.ArticlePublishServiceable;
import com.ewcms.publication.service.TemplateSourcePublishServiceable;
import com.ewcms.publication.task.TaskException;
import com.ewcms.publication.task.Taskable;
import com.ewcms.publication.task.impl.event.CompleteEvent;
import com.ewcms.publication.task.impl.process.GeneratorProcess;
import com.ewcms.publication.task.impl.process.TaskProcessable;
import com.ewcms.publication.uri.UriRuleable;
import com.ewcms.publication.uri.UriRules;

import freemarker.template.Configuration;

/**
 * 发布List页面任务
 * 
 * @author wangwei
 */
public class ListTask extends TaskBase{
    private static final Logger logger = LoggerFactory.getLogger(ListTask.class);
    
    public static class Builder{
        private final Configuration cfg;
        private final TemplateSourcePublishServiceable templateSourceService;
        private final ArticlePublishServiceable articleService;
        private final Site site;
        private final Channel channel;
        private final String path;
        private String username;
        private String uriRulePatter;
        private boolean independence = true;
        private List<Taskable> dependences;
        
        public Builder(Configuration cfg,
                TemplateSourcePublishServiceable templateSourceService,
                ArticlePublishServiceable articleService,
                Site site,Channel channel,String path){
            
            this.cfg = cfg;
            this.templateSourceService = templateSourceService;
            this.articleService = articleService;
            this.site = site;
            this.channel = channel;
            this.path = path;
        }
        
        public Builder setUsername(String username){
            this.username = username;
            return this;
        }
        
        public Builder setUriRulePatter(String patter){
            this.uriRulePatter = patter;
            return this;
        }
        
        public Builder dependence(){
            this.independence = true;
            return this;
        }
        
        private List<Taskable> getDependenceTasks(){
            List<Taskable> dependences = new ArrayList<Taskable>();
            if(independence){
                dependences.add(new  TemplateSourceTask.Builder(
                        templateSourceService, site.getId()).
                        builder());
            }
            return dependences;
        }
        
        public ListTask build(){
            dependences = getDependenceTasks();
            return new ListTask(this);
        }
    }
    
    private final Builder builder;
    
    public ListTask(Builder builder){
        super(newTaskId());
        this.builder = builder;
    }
    
    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer getSiteId() {
        return builder.site.getId();
    }

    @Override
    public String getUsername() {
        return builder.username;
    }

    @Override
    public List<Taskable> getDependences() {
        return builder.dependences;
    }

    @Override
    public void close() throws TaskException {
        // do not instance
        
    }

    /**TaskProcessable
     * 得到频道需要发布的页数
     * 
     * @param channelId 频道编号
     * @param row 每页行数builder
     * @return 总页数
     */
    private int getPageCount(int channelId, int row){
        Integer count = builder.articleService.getArticleCount(channelId);
        logger.debug("Article count is {}",count);
        return (count + row -1)/row;
    }
    
    @Override
    protected List<TaskProcessable> getTaskProcesses() throws TaskException {
        int pageCount = getPageCount(builder.channel.getId(),builder.channel.getListSize());
        logger.debug("Page count is {}",pageCount);
        List<TaskProcessable> processes = new ArrayList<TaskProcessable>();
        UriRuleable uriRule = (StringUtils.isBlank(builder.uriRulePatter) ? 
                UriRules.newList() : UriRules.newUriRuleBy(builder.uriRulePatter));
        for(int page = 0 ; page < pageCount ; page++){
            Generatorable generator = 
                new ListGenerator(builder.cfg,builder.site,builder.channel,uriRule,page,pageCount);
            TaskProcessable process = new GeneratorProcess(generator,builder.path);
            process.registerEvent(new CompleteEvent(this));
            processes.add(process);
        }
        return processes;
    }
}