/***********************************************************************
 * Copyright 2023 Turker Ozturk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***********************************************************************/


package fat32libproject.structures.dataarea;

/**
 * bunu ben kendim olusturdum. gerek olmayabilir bu yapiya.
 * @author u
 */
public class DataCluster {
    
    private long clusterId;
    private DataClusterType dataClusterType;
    private int slackSpaceOffsetRelativeToCluster;

    public long getClusterId() {
        return clusterId;
    }

    public void setClusterId(long clusterId) {
        this.clusterId = clusterId;
    }

    public DataClusterType getDataClusterType() {
        return dataClusterType;
    }

    public void setDataClusterType(DataClusterType dataClusterType) {
        this.dataClusterType = dataClusterType;
    }

    public int getSlackSpaceOffsetRelativeToCluster() {
        return slackSpaceOffsetRelativeToCluster;
    }

    public void setSlackSpaceOffsetRelativeToCluster(int slackSpaceOffsetRelativeToCluster) {
        this.slackSpaceOffsetRelativeToCluster = slackSpaceOffsetRelativeToCluster;
    }
    
    
}
