package uk.co.ankeetpatel.encryptedfilesystem.efsserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.domain.SidRetrievalStrategyImpl;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.File;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.User;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LocalPermissionService {

    @Autowired
    private MutableAclService aclService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    public void updatePermissionForUser(File targetObj, Permission permission, String username, boolean permissionType) {
        final Sid sid = new PrincipalSid(username);
        doUpdateOnPermissions(targetObj, permission, permissionType, sid);
    }

    public void updatePermissionForAuthority(File targetObj, Permission permission, String authority, boolean permissionType) {
        final Sid sid = new GrantedAuthoritySid(authority);
        doUpdateOnPermissions(targetObj, permission, permissionType, sid);
    }

    private void doUpdateOnPermissions(File targetObj, Permission permission, boolean permissionType, Sid sid) {
        Integer checkPermission = checkPermission(targetObj, permission, sid);
        if (permissionType && checkPermission == 1) {
            //If you want them to have the permission and they already have it. Do nothing
        } else if (!permissionType && checkPermission == 2) {
            //If you dont want them to have the permission and they have been denied it in file. Remove permission as ACL auto handles this.
            removePermissionForSid(targetObj, permission, sid);
        }else if (!permissionType && checkPermission == 1) {
            //If you dont want them to have permission and they have it already
            removePermissionForSid(targetObj, permission, sid);
        } else if (permissionType && checkPermission == 3) {
            //If you want them to have the permission and they dont have any permission entry
            addPermissionForSid(targetObj, permission, sid);
        }else if(!permissionType && checkPermission == 1) {
            //If they have permission and you dont want them to have permission
        }else {
            //If you dont want them to have access and they have no permission node.
            //Do nothing as ACL auto sets no access if they dont have a granting permission value.
        }
    }

    @PreAuthorize("hasPermission(targetObj, 'ADMINISTRATION')")
    private void addPermissionForSid(File targetObj, Permission permission, Sid sid) {
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);

        tt.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final ObjectIdentity oi = new ObjectIdentityImpl(targetObj.getClass(), targetObj.getId());

                MutableAcl acl = null;
                try {
                    acl = (MutableAcl) aclService.readAclById(oi);
                } catch (final NotFoundException nfe) {
                    acl = aclService.createAcl(oi);
                }
                acl.insertAce(acl.getEntries().size(), permission, sid, true);
                aclService.updateAcl(acl);
            }
        });
    }

    @PreAuthorize("hasPermission(targetObj, 'ADMINISTRATION')")
    private void removePermissionForSid(File targetObj, Permission permission, Sid sid) {
        ObjectIdentity oi = new ObjectIdentityImpl(targetObj.getClass(), targetObj.getId());
        try {
            MutableAcl acl = (MutableAcl) aclService.readAclById(oi);
            List<AccessControlEntry> aclEntries = acl.getEntries();
            for (int i = aclEntries.size() - 1; i >= 0; i--) {
                AccessControlEntry ace = aclEntries.get(i);
                if (ace.getSid().equals(sid) && ace.getPermission() == permission) {
                    acl.deleteAce(i);
                }
            }
            if (acl.getEntries().isEmpty()) {
                aclService.deleteAcl(oi, true);
            }
            aclService.updateAcl(acl);
        } catch (NotFoundException ignore) {
        }
    }

    private Integer checkPermission(File targetObj, Permission permission, Sid sid) {
        ObjectIdentity oi = new ObjectIdentityImpl(targetObj.getClass(), targetObj.getId());
        try {
            MutableAcl acl = (MutableAcl) aclService.readAclById(oi);
            List<AccessControlEntry> aclEntries = acl.getEntries();
            for (int i = aclEntries.size() - 1; i >= 0; i--) {
                AccessControlEntry ace = aclEntries.get(i);
                if (ace.getSid().equals(sid) && ace.getPermission() == permission && ace.isGranting()) {
                    //Has sid and permission and is granting permission to user
                    return 1;
                } else if (ace.getSid().equals(sid) && ace.getPermission() == permission && !ace.isGranting()) {
                    //Has sid and permission but is not granting permission to user. Logically should never be called.
                    return 2;
                } else {
                    //Either sid or permission do not match with what we are searching for. Should ignore this ace
                    //as it is not relevant to the query.
                }
            }
        } catch (NotFoundException ignore) {
            System.err.println("File entry not found.");
        }
        return 3;
    }

    public List<Integer> getPermissions(File targetObj, Authentication authentication) {
        final ObjectIdentity oid = new ObjectIdentityImpl(targetObj.getClass(), targetObj.getId());

        SidRetrievalStrategy sidRetrievalStrategy = new SidRetrievalStrategyImpl();
        List<Sid> sids = sidRetrievalStrategy.getSids(authentication);
        // Lookup only ACLs for SIDs we're interested in
        Acl acl = aclService.readAclById(oid, sids);
        List<AccessControlEntry> aces = acl.getEntries();
        List<Integer> permissionsList = new ArrayList<>();
        for (AccessControlEntry ace : aces ) {
            if(sids.contains(ace.getSid())) {
                if(!permissionsList.contains(ace.getPermission().getMask())) {
                    permissionsList.add(ace.getPermission().getMask());
                }
            }
        }
        return permissionsList;
    }

    public List<Integer> getPermissionsForUser(File targetObj, Authentication authentication, User user) {
        final ObjectIdentity oid = new ObjectIdentityImpl(targetObj.getClass(), targetObj.getId());

        SidRetrievalStrategy sidRetrievalStrategy = new SidRetrievalStrategyImpl();
        List<Sid> sids = sidRetrievalStrategy.getSids(authentication);
        // Lookup only ACLs for SIDs we're interested in
        Acl acl = aclService.readAclById(oid, sids);
        List<AccessControlEntry> aces = acl.getEntries();
        List<Integer> permissionsList = new ArrayList<>();
        for (AccessControlEntry ace : aces ) {
            if(ace.getSid().toString().equalsIgnoreCase("PrincipalSid[" + user.getUsername() + "]")) {
                if(!permissionsList.contains(ace.getPermission().getMask())) {
                    permissionsList.add(ace.getPermission().getMask());
                }
            }
        }
        return permissionsList;
    }
}
