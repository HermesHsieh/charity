package tw.org.by37.supplieshelp;

import tw.org.by37.organization.OrganizationData;

public class SupportData {

        private String id;
        private String name;
        private String description;
        private String organizationId;
        private String total;
        private String created_at;
        private String updated_at;
        private String goodsTypeId;
        private OrganizationData[] organization;

        public void setId(String id) {
                this.id = id;
        }

        public String getId() {
                return id;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getName() {
                return name;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public String getDescription() {
                return description;
        }

        public void setOrganizationId(String organizationId) {
                this.organizationId = organizationId;
        }

        public String getOrganizationId() {
                return organizationId;
        }

        public void setTotal(String total) {
                this.total = total;
        }

        public String getTotal() {
                return total;
        }

        public void setCreated_At(String created_at) {
                this.created_at = created_at;
        }

        public String getCreated_At() {
                return created_at;
        }

        public void setUpdated_At(String updated_at) {
                this.updated_at = updated_at;
        }

        public String getUpdated_At() {
                return updated_at;
        }

        public void setGoodsTypeId(String goodsTypeId) {
                this.goodsTypeId = goodsTypeId;
        }

        public String getGoodsTypeId() {
                return goodsTypeId;
        }

        public void setOrganizationData(OrganizationData[] organization) {
                this.organization = organization;
        }

        public OrganizationData[] getOrganizationData() {
                return organization;
        }

}
