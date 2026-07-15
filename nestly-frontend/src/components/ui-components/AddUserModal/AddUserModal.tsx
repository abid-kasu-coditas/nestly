import { Controller, useForm } from 'react-hook-form'
import styles from './AddUserModal.module.scss'
import type { AddUserData } from './AddUserModal.types';
import { PrimaryBtn, SecondaryBtn } from '../Button/Button';
import { useAddUserMutation } from '../../../redux/slices/adminApiSlice';

const AddUserModal = () => {

    const { handleSubmit, control, formState: { isLoading }} = useForm<AddUserData>();

    const [ addUser ] = useAddUserMutation();

    const handleAddUser = async(data: AddUserData) => {
        console.log("adding user");
        const response = await addUser(data).unwrap();
        console.log(response);
        
    }

  return (
    <div className={styles.AddUserModal}>
        <div className={styles.AddUserModalContainer}>
            <form onSubmit={handleSubmit(handleAddUser)} className={styles.AddUserModalForm}>
                <h2>Add User</h2>
                <Controller 
                    name='email'
                    control={control}
                    render={({field})=>{
                        return <input className={styles.Input} placeholder='user@example.com' {...field}></input>
                    }}
                />
                <Controller 
                    name='role'
                    control={control}
                    render={({field})=>{
                        return <select className={styles.Input} defaultValue={"none"} {...field}>
                            <option value="none" disabled>Select Role</option>
                            <option value="TENANT">Tenant</option>
                            <option value="OWNER">Owner</option>
                        </select>
                    }}
                />
                <div className={styles.BtnContainer}>
                    <SecondaryBtn>Cancel</SecondaryBtn>
                    <PrimaryBtn>{isLoading ? "Adding..." : "Add"}</PrimaryBtn>
                </div>
            </form>
        </div>
    </div>
  )
}

export default AddUserModal