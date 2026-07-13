import styles from "./Button.module.scss";
import type { BtnProps } from "./Button.types";

export const PrimaryBtn = ({ className, children, ...props}: BtnProps) => {
    return (
        <button className={[styles.PrimaryBtn, className].join(" ")} {...props}>{children}</button>
    )
}
