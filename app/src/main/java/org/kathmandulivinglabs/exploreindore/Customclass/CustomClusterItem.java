package org.kathmandulivinglabs.exploreindore.Customclass;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.plugins.cluster.clustering.ClusterItem;

/**
 * Created by Bhawak on 4/9/18.
 */

public interface CustomClusterItem extends ClusterItem {
    Icon getIcon();
}
